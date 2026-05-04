package com.example.weddingInvitation_b.service;

import com.example.weddingInvitation_b.dto.response.AddressSearchResultDto;

/**
 * 주소 검색 및 지도 이미지 서비스 인터페이스
 *
 * <p>카카오 로컬 API를 활용한 주소 검색과
 * 네이버 Static Map API를 활용한 지도 이미지 반환을 담당한다.</p>
 *
 * @see com.example.weddingInvitation_b.service.impl.AddressServiceImpl
 */
public interface AddressService {

    /**
     * 주소 키워드 검색
     *
     * @param query 검색 키워드
     * @return 주소 검색 결과 목록 (도로명 주소, 지번 주소, 위도, 경도 포함)
     */
    AddressSearchResultDto searchAddress(String query);

    /**
     * 위경도 좌표 기반 지도 이미지 조회
     *
     * <p>네이버 Static Map API를 통해 해당 좌표의 지도 이미지를 PNG로 반환한다.</p>
     *
     * @param lat    위도
     * @param lng    경도
     * @param width  이미지 너비 (px)
     * @param height 이미지 높이 (px)
     * @return 지도 이미지 바이트 배열 (PNG)
     */
    byte[] getMapImage(double lat, double lng, int width, int height);
}
