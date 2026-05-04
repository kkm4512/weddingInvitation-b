package com.example.weddingInvitation_b.service.impl;

import com.example.weddingInvitation_b.client.KakaoLocalClient;
import com.example.weddingInvitation_b.client.NaverMapClient;
import com.example.weddingInvitation_b.dto.response.AddressSearchResultDto;
import com.example.weddingInvitation_b.dto.response.KakaoAddressResponseDto;
import com.example.weddingInvitation_b.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 주소 검색 및 지도 이미지 서비스 구현체
 *
 * <p>KakaoLocalClient를 통해 주소 검색을 수행하고,
 * NaverMapClient를 통해 지도 이미지를 반환한다.</p>
 *
 * @see AddressService
 * @see KakaoLocalClient
 * @see NaverMapClient
 */
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final KakaoLocalClient kakaoLocalClient;
    private final NaverMapClient naverMapClient;

    /**
     * 주소 키워드 검색
     *
     * <p>카카오 로컬 API를 호출하여 키워드에 매칭되는 주소 목록을 반환한다.
     * 원시 카카오 응답을 {@link AddressSearchResultDto}로 변환하여 반환한다.</p>
     *
     * @param query 검색 키워드 (예: "강남구 테헤란로")
     * @return 프론트엔드용 주소 목록 DTO (최대 10건)
     */
    @Override
    public AddressSearchResultDto searchAddress(String query) {
        KakaoAddressResponseDto kakaoResponse = kakaoLocalClient.searchAddress(query);
        return AddressSearchResultDto.from(kakaoResponse);
    }

    /**
     * 위경도 좌표 기반 지도 이미지 조회
     *
     * <p>네이버 Static Map API를 호출하여 해당 좌표의 지도 이미지를 PNG로 반환한다.</p>
     *
     * @param lat    위도
     * @param lng    경도
     * @param width  이미지 너비 (px)
     * @param height 이미지 높이 (px)
     * @return 지도 이미지 바이트 배열 (PNG)
     */
    @Override
    public byte[] getMapImage(double lat, double lng, int width, int height) {
        return naverMapClient.getStaticMapImage(lat, lng, width, height);
    }
}
