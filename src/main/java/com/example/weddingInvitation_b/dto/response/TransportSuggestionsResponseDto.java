package com.example.weddingInvitation_b.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 교통수단 자동 추천 응답 DTO
 *
 * <p>예식장 좌표 기반으로 외부 API를 통해 조회한
 * 주변 지하철역·버스정류장·주차장 목록을 담아 반환한다.</p>
 *
 * <ul>
 *   <li>지하철역: 카카오 카테고리 검색 API (SW8) — 구현 완료</li>
 *   <li>버스정류장: 외부 API 미선정 (Tmap POI API 또는 공공데이터포털 TAGO 후보) — 미구현</li>
 *   <li>주차장: 카카오 카테고리 검색 API (PK6) 또는 미정 — 미구현</li>
 * </ul>
 *
 * @see KakaoCategoryResponseDto 지하철 카테고리 검색 원시 응답
 */
@Getter
@Builder
public class TransportSuggestionsResponseDto {

    /** 반경 내 지하철역 목록 (거리 오름차순, 최대 5건) */
    private List<TransportItem> subways;

    /** 반경 내 버스정류장 목록 (거리 오름차순, 최대 5건) — 외부 API 선정 후 구현 예정 */
    private List<TransportItem> buses;

    /** 반경 내 주차장 목록 (거리 오름차순, 최대 5건) — 미구현 */
    private List<TransportItem> parkings;

    /**
     * 교통수단 단건
     */
    @Getter
    @Builder
    public static class TransportItem {

        /** 장소명 (예: 역삼역 3번출구, 강남역.신분당선) */
        private String name;

        /** 중심 좌표로부터의 직선거리 (m) */
        private Integer distance;

        /** 도로명 주소 (없을 경우 지번 주소) */
        private String address;
    }

    /**
     * 카카오 카테고리 검색 원시 응답을 {@link TransportItem} 목록으로 변환한다.
     *
     * <p>변환 규칙:</p>
     * <ul>
     *   <li>지하철역(SW8): 동일 역명(첫 번째 공백 이전 토큰)이 중복될 경우 거리가 가장 짧은 1건만 유지</li>
     *   <li>버스정류장(BS8): 중복 제거 없이 거리 오름차순 그대로 반환</li>
     *   <li>결과는 거리 오름차순 정렬 후 최대 {@code limit}건으로 슬라이싱</li>
     * </ul>
     *
     * @param response 카카오 카테고리 검색 원시 응답 (null 허용)
     * @param dedupByStation 지하철 역명 기준 중복 제거 여부
     * @param limit          반환 최대 건수
     * @return 변환된 교통수단 목록 (응답이 null이면 빈 리스트)
     */
    public static List<TransportItem> fromKakaoResponse(
            KakaoCategoryResponseDto response,
            boolean dedupByStation,
            int limit) {

        if (response == null || response.getDocuments() == null) {
            return Collections.emptyList();
        }

        List<KakaoCategoryResponseDto.Document> docs = response.getDocuments();

        if (dedupByStation) {
            // 역명 앞 토큰(공백 이전) 기준으로 그룹화 → 그룹 내 거리 최소값 1건만 유지
            Map<String, KakaoCategoryResponseDto.Document> stationMap = docs.stream()
                .filter(d -> d.getPlaceName() != null)
                .collect(Collectors.toMap(
                    d -> extractStationName(d.getPlaceName()),
                    d -> d,
                    (existing, next) -> parseDistance(existing.getDistance()) <= parseDistance(next.getDistance())
                        ? existing : next
                ));
            docs = stationMap.values().stream()
                .sorted(Comparator.comparingInt(d -> parseDistance(d.getDistance())))
                .collect(Collectors.toList());
        }

        return docs.stream()
            .filter(d -> d.getPlaceName() != null)
            .map(d -> TransportItem.builder()
                .name(d.getPlaceName())
                .distance(parseDistance(d.getDistance()))
                .address(resolveAddress(d))
                .build())
            .sorted(Comparator.comparingInt(TransportItem::getDistance))
            .limit(limit)
            .collect(Collectors.toList());
    }

    /**
     * 장소명에서 역명 토큰을 추출한다.
     *
     * <p>예: "역삼역 3번출구" → "역삼역", "강남역.신분당선" → "강남역"</p>
     */
    private static String extractStationName(String placeName) {
        // 공백 또는 '.' 이전 첫 토큰을 역명으로 간주
        String token = placeName.split("[ .]")[0];
        return token.isEmpty() ? placeName : token;
    }

    /**
     * distance 문자열을 Integer로 변환한다.
     *
     * @param distance 카카오 distance 필드 ("320" 형태, null 가능)
     * @return 정수 거리 (파싱 실패 또는 null이면 Integer.MAX_VALUE)
     */
    private static int parseDistance(String distance) {
        if (distance == null || distance.isBlank()) {
            return Integer.MAX_VALUE;
        }
        try {
            return Integer.parseInt(distance.trim());
        } catch (NumberFormatException e) {
            return Integer.MAX_VALUE;
        }
    }

    /**
     * 카테고리 검색 결과에서 도로명 주소를 우선하여 반환하고, 없으면 지번 주소를 반환한다.
     */
    private static String resolveAddress(KakaoCategoryResponseDto.Document doc) {
        String road = doc.getRoadAddressName();
        if (road != null && !road.isBlank()) {
            return road;
        }
        return Objects.requireNonNullElse(doc.getAddressName(), "");
    }
}
