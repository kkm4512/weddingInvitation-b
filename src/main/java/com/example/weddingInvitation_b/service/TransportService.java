package com.example.weddingInvitation_b.service;

import com.example.weddingInvitation_b.dto.response.SubwayItem;
import com.example.weddingInvitation_b.dto.response.TransportSuggestionsResponseDto.TransportItem;

import java.util.List;

/**
 * 교통수단 조회 서비스 인터페이스
 *
 * <p>예식장 좌표 기반으로 주변 교통수단을 종류별로 분리하여 조회한다.
 * 각 메서드는 독립적인 외부 API를 통해 단일 교통수단 유형의 결과만 반환한다.</p>
 *
 * @see com.example.weddingInvitation_b.service.impl.TransportServiceImpl
 */
public interface TransportService {

    /**
     * 좌표 기반 주변 지하철역 출구 조회
     *
     * <p>카카오 카테고리 검색 API (SW8)로 인근 역명을 파악한 뒤,
     * 카카오 키워드 검색으로 각 역의 출구별 좌표를 조회하여
     * 예식장에서 가장 가까운 출구를 역별로 1건씩 반환한다.</p>
     *
     * @param lat    위도
     * @param lng    경도
     * @param radius 탐색 반경 (m)
     * @return 주변 지하철역 출구 목록 (도보시간 오름차순, 최대 5건)
     */
    List<SubwayItem> getSubways(double lat, double lng, int radius);

    /**
     * 좌표 기반 주변 버스정류장 조회
     *
     * <p>외부 API 미선정으로 현재 빈 리스트를 반환한다.
     * (후보: Tmap POI API, 공공데이터포털 TAGO API)</p>
     *
     * @param lat    위도
     * @param lng    경도
     * @param radius 탐색 반경 (m)
     * @return 주변 버스정류장 목록 (현재 항상 빈 리스트)
     */
    List<TransportItem> getBuses(double lat, double lng, int radius);

    /**
     * 좌표 기반 주변 주차장 조회
     *
     * <p>카카오 카테고리 검색 API (PK6)를 호출하여 반경 내 주차장을 반환한다.</p>
     *
     * @param lat    위도
     * @param lng    경도
     * @param radius 탐색 반경 (m)
     * @return 주변 주차장 목록 (거리 오름차순, 최대 5건)
     */
    List<TransportItem> getParkings(double lat, double lng, int radius);
}
