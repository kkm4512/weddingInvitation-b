package com.example.weddingInvitation_b.service.impl;

import com.example.weddingInvitation_b.client.KakaoLocalClient;
import com.example.weddingInvitation_b.client.NaverMapClient;
import com.example.weddingInvitation_b.dto.response.AddressSearchResultDto;
import com.example.weddingInvitation_b.dto.response.KakaoAddressResponseDto;
import com.example.weddingInvitation_b.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final KakaoLocalClient kakaoLocalClient;
    private final NaverMapClient naverMapClient;

    /**
     * 주소 키워드 검색
     *
     * <p>카카오 로컬 API를 호출하여 키워드에 매칭되는 주소 목록을 반환한다.</p>
     *
     * @param query 검색 키워드
     * @param page  페이지 번호 (1부터 시작)
     * @param size  페이지당 결과 수
     * @return 프론트엔드용 주소 목록 DTO
     */
    @Override
    public AddressSearchResultDto searchAddress(String query, int page, int size) {
        KakaoAddressResponseDto kakaoResponse = kakaoLocalClient.searchAddress(query, page, size);
        return AddressSearchResultDto.from(kakaoResponse, page, size);
    }

    /**
     * 위경도 좌표 기반 지도 이미지 조회
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
