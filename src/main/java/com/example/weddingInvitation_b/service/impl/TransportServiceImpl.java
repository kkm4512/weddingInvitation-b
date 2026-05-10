package com.example.weddingInvitation_b.service.impl;

import com.example.weddingInvitation_b.client.KakaoLocalClient;
import com.example.weddingInvitation_b.dto.response.KakaoCategoryResponseDto;
import com.example.weddingInvitation_b.dto.response.TransportSuggestionsResponseDto;
import com.example.weddingInvitation_b.dto.response.TransportSuggestionsResponseDto.TransportItem;
import com.example.weddingInvitation_b.service.TransportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 교통수단 조회 서비스 구현체
 *
 * <p>지하철역·버스정류장·주차장을 종류별로 독립적으로 조회한다.
 * 지하철역과 주차장은 카카오 카테고리 검색 API를 통해 조회하며,
 * 버스정류장은 외부 API 미선정으로 빈 리스트를 반환한다.</p>
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

    private final KakaoLocalClient kakaoLocalClient;

    /**
     * 좌표 기반 주변 지하철역 조회
     *
     * <p>카카오 카테고리 검색 API (SW8)를 호출한다.
     * 동일 역명의 여러 출구는 가장 가까운 1건으로 중복 제거 후 반환한다.
     * API 호출 실패 시 경고 로그를 남기고 빈 리스트를 반환한다.</p>
     *
     * @param lat    위도
     * @param lng    경도
     * @param radius 탐색 반경 (m)
     * @return 주변 지하철역 목록 (거리 오름차순, 최대 5건)
     */
    @Override
    public List<TransportItem> getSubways(double lat, double lng, int radius) {
        try {
            KakaoCategoryResponseDto response =
                kakaoLocalClient.searchByCategory(KakaoLocalClient.CATEGORY_SUBWAY, lng, lat, radius);

            // [RAW 데이터 확인용 임시 로그] place_name, category_name, distance 원본 출력
            if (response != null && response.getDocuments() != null) {
                response.getDocuments().forEach(doc ->
                    log.info("[RAW] place_name={} | category_name={} | distance={}",
                        doc.getPlaceName(), doc.getCategoryName(), doc.getDistance())
                );
            }

            return TransportSuggestionsResponseDto.fromKakaoResponse(response, true, TRANSPORT_RESULT_LIMIT);
        } catch (Exception e) {
            log.warn("[TransportServiceImpl] 지하철역 카테고리 검색 실패 (lat={}, lng={}): {}",
                lat, lng, e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * 좌표 기반 주변 버스정류장 조회
     *
     * <p>외부 API 미선정으로 현재 빈 리스트를 반환한다.
     * (후보: Tmap POI API, 공공데이터포털 TAGO API)</p>
     *
     * @param lat    위도
     * @param lng    경도
     * @param radius 탐색 반경 (m)
     * @return 빈 리스트 (TODO: 외부 API 선정 후 구현)
     */
    @Override
    public List<TransportItem> getBuses(double lat, double lng, int radius) {
        // TODO: 버스정류장 외부 API 선정 후 구현 (Tmap POI API 또는 공공데이터포털 TAGO)
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
