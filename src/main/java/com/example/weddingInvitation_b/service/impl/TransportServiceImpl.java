package com.example.weddingInvitation_b.service.impl;

import com.example.weddingInvitation_b.client.KakaoLocalClient;
import com.example.weddingInvitation_b.dto.response.KakaoAddressResponseDto;
import com.example.weddingInvitation_b.dto.response.KakaoCategoryResponseDto;
import com.example.weddingInvitation_b.dto.response.SubwayItem;
import com.example.weddingInvitation_b.dto.response.TransportSuggestionsResponseDto;
import com.example.weddingInvitation_b.dto.response.TransportSuggestionsResponseDto.TransportItem;
import com.example.weddingInvitation_b.service.TransportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 교통수단 조회 서비스 구현체
 *
 * <p>지하철역·버스정류장·주차장을 종류별로 독립적으로 조회한다.</p>
 *
 * <ul>
 *   <li>지하철: 카카오 SW8(역 탐색) + 키워드 검색(출구 좌표) 2단계 조회</li>
 *   <li>버스: 외부 API 미선정 → 빈 리스트 반환</li>
 *   <li>주차: 카카오 PK6 카테고리 검색</li>
 * </ul>
 *
 * @see TransportService
 * @see KakaoLocalClient
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransportServiceImpl implements TransportService {

    /** 교통수단 추천 결과 반환 최대 건수 */
    private static final int TRANSPORT_RESULT_LIMIT = 5;

    /** SW8 결과에서 출구 검색할 최대 역 수 */
    private static final int STATION_SEARCH_LIMIT = 3;

    /** 각 역당 시도할 최대 출구 번호 (대부분의 역은 10번 이하) */
    private static final int MAX_EXIT_NUMBER = 10;

    /** 도보 속도 (m/분) */
    private static final double WALKING_SPEED_M_PER_MIN = 67.0;

    private final KakaoLocalClient kakaoLocalClient;

    /**
     * 좌표 기반 주변 지하철역 출구 조회
     *
     * <p>1단계: 카카오 SW8 카테고리 검색으로 인근 역명 파악<br>
     * 2단계: 각 역의 1~{@value MAX_EXIT_NUMBER}번 출구를 키워드 검색하여 가장 가까운 출구 선택<br>
     * 3단계: place_name 파싱으로 노선명·출구번호 추출, 거리 기반 도보시간 계산</p>
     *
     * @param lat    위도
     * @param lng    경도
     * @param radius 탐색 반경 (m)
     * @return 주변 지하철역 출구 목록 (도보시간 오름차순, 최대 5건)
     */
    @Override
    public List<SubwayItem> getSubways(double lat, double lng, int radius) {
        try {
            // 1단계: SW8 카테고리 검색 → 인근 역명 목록 추출 (최대 STATION_SEARCH_LIMIT개)
            KakaoCategoryResponseDto sw8Response =
                kakaoLocalClient.searchByCategory(KakaoLocalClient.CATEGORY_SUBWAY, lng, lat, radius);

            if (sw8Response == null || sw8Response.getDocuments() == null
                    || sw8Response.getDocuments().isEmpty()) {
                return Collections.emptyList();
            }

            Set<String> stationNames = new LinkedHashSet<>();
            for (KakaoCategoryResponseDto.Document doc : sw8Response.getDocuments()) {
                String placeName = doc.getPlaceName();
                if (placeName != null && placeName.contains("역")) {
                    int yeokIdx = placeName.indexOf("역");
                    stationNames.add(placeName.substring(0, yeokIdx + 1)); // "서울숲역"
                }
                if (stationNames.size() >= STATION_SEARCH_LIMIT) break;
            }

            // 2단계: 각 역의 가장 가까운 출구 탐색
            List<SubwayItem> result = new ArrayList<>();
            for (String stationName : stationNames) {
                SubwayItem nearest = findNearestExit(stationName, lng, lat, radius);
                if (nearest != null) {
                    result.add(nearest);
                }
            }

            return result.stream()
                .sorted(Comparator.comparingInt(SubwayItem::getWalkingMinutes))
                .limit(TRANSPORT_RESULT_LIMIT)
                .collect(Collectors.toList());

        } catch (Exception e) {
            log.warn("[TransportServiceImpl] 지하철역 조회 실패 (lat={}, lng={}): {}", lat, lng, e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * 특정 역의 출구 중 예식장에서 가장 가까운 출구를 반환한다.
     *
     * <p>1번부터 {@value MAX_EXIT_NUMBER}번까지 순서대로 키워드 검색하여
     * category_name에 "지하철출구"가 포함된 결과 중 distance가 가장 작은 출구를 선택한다.</p>
     *
     * @param stationName 역명 (예: "서울숲역")
     * @param lng         중심 경도
     * @param lat         중심 위도
     * @param radius      탐색 반경 (m)
     * @return 가장 가까운 출구 정보, 출구를 찾지 못하면 null
     */
    private SubwayItem findNearestExit(String stationName, double lng, double lat, int radius) {
        SubwayItem nearest = null;
        int minDist = Integer.MAX_VALUE;

        for (int exitNum = 1; exitNum <= MAX_EXIT_NUMBER; exitNum++) {
            String query = stationName + " " + exitNum + "번출구";
            try {
                KakaoAddressResponseDto exitResponse =
                    kakaoLocalClient.searchKeywordNearby(query, lng, lat, radius);

                if (exitResponse == null || exitResponse.getDocuments() == null) continue;

                for (KakaoAddressResponseDto.Document doc : exitResponse.getDocuments()) {
                    // "지하철출구" 카테고리이고, 해당 역명으로 시작하는 결과만 유효
                    if (!isSubwayExit(doc.getCategoryName())) continue;
                    if (doc.getPlaceName() == null || !doc.getPlaceName().startsWith(stationName)) continue;

                    int dist = parseDistance(doc.getDistance());
                    if (dist < minDist) {
                        minDist = dist;
                        nearest = toSubwayItem(doc, dist);
                    }
                    break; // 역명이 일치하는 첫 번째 결과만 사용
                }
            } catch (Exception e) {
                log.warn("[TransportServiceImpl] 출구 검색 실패 query={}: {}", query, e.getMessage());
            }
        }

        return nearest;
    }

    /**
     * category_name이 지하철 출구인지 확인한다.
     *
     * @param categoryName 카카오 카테고리 전체 경로
     * @return "지하철출구" 포함 여부
     */
    private boolean isSubwayExit(String categoryName) {
        return categoryName != null && categoryName.contains("지하철출구");
    }

    /**
     * 카카오 키워드 검색 문서를 {@link SubwayItem}으로 변환한다.
     *
     * <p>place_name 파싱 규칙:</p>
     * <pre>
     * "서울숲역 수인분당선 5번출구"
     *   → stationName = "서울숲역"
     *   → lineName    = "수인분당선"
     *   → exitNumber  = "5번 출구"
     * </pre>
     *
     * @param doc  카카오 키워드 검색 문서
     * @param dist 직선거리 (m)
     * @return 변환된 SubwayItem
     */
    private SubwayItem toSubwayItem(KakaoAddressResponseDto.Document doc, int dist) {
        String placeName = doc.getPlaceName(); // "서울숲역 수인분당선 5번출구"

        // 역명: "역" 이전 + "역" 포함
        int yeokIdx = placeName.indexOf("역");
        String stationName = placeName.substring(0, yeokIdx + 1);

        // 나머지: "수인분당선 5번출구"
        String remainder = placeName.substring(yeokIdx + 1).trim();
        String[] parts = remainder.split(" ", 2);

        String lineName   = parts.length > 0 ? parts[0] : "";
        String exitRaw    = parts.length > 1 ? parts[1] : ""; // "5번출구"
        String exitNumber = exitRaw.replace("번출구", "번 출구"); // "5번 출구"

        int walkingMinutes = Math.max(1, (int) Math.ceil(dist / WALKING_SPEED_M_PER_MIN));

        return SubwayItem.builder()
            .stationName(stationName)
            .lineName(lineName)
            .exitNumber(exitNumber)
            .walkingMinutes(walkingMinutes)
            .build();
    }

    /**
     * distance 문자열을 int로 변환한다.
     *
     * @param distance 카카오 distance 필드 ("14" 형태, null 가능)
     * @return 정수 거리 (파싱 실패 또는 null이면 Integer.MAX_VALUE)
     */
    private int parseDistance(String distance) {
        if (distance == null || distance.isBlank()) return Integer.MAX_VALUE;
        try {
            return Integer.parseInt(distance.trim());
        } catch (NumberFormatException e) {
            return Integer.MAX_VALUE;
        }
    }

    /**
     * 좌표 기반 주변 버스정류장 조회
     *
     * <p>외부 API 미선정으로 현재 빈 리스트를 반환한다.
     * (후보: 공공데이터포털 TAGO API, 서울 열린데이터광장 버스 API)</p>
     *
     * @param lat    위도
     * @param lng    경도
     * @param radius 탐색 반경 (m)
     * @return 빈 리스트 (TODO: 외부 API 선정 후 구현)
     */
    @Override
    public List<TransportItem> getBuses(double lat, double lng, int radius) {
        // TODO: 버스정류장 외부 API 선정 후 구현 (공공데이터포털 TAGO API 또는 서울 열린데이터광장)
        return Collections.emptyList();
    }

    /**
     * 좌표 기반 주변 주차장 조회
     *
     * <p>카카오 카테고리 검색 API (PK6)를 호출한다.
     * API 호출 실패 시 경고 로그를 남기고 빈 리스트를 반환한다.</p>
     *
     * @param lat    위도
     * @param lng    경도
     * @param radius 탐색 반경 (m)
     * @return 주변 주차장 목록 (거리 오름차순, 최대 5건)
     */
    @Override
    public List<TransportItem> getParkings(double lat, double lng, int radius) {
        try {
            KakaoCategoryResponseDto response =
                kakaoLocalClient.searchByCategory(KakaoLocalClient.CATEGORY_PARKING, lng, lat, radius);
            return TransportSuggestionsResponseDto.fromKakaoResponse(response, false, TRANSPORT_RESULT_LIMIT);
        } catch (Exception e) {
            log.warn("[TransportServiceImpl] 주차장 카테고리 검색 실패 (lat={}, lng={}): {}",
                lat, lng, e.getMessage());
            return Collections.emptyList();
        }
    }
}
