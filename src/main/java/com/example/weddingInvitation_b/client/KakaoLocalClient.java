package com.example.weddingInvitation_b.client;

import com.example.weddingInvitation_b.dto.response.KakaoAddressResponseDto;
import com.example.weddingInvitation_b.dto.response.KakaoCategoryResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 카카오 로컬 API 통신 클라이언트
 *
 * <p>예식 장소 편집에 필요한 세 가지 기능을 전담한다.</p>
 *
 * <ul>
 *   <li>장소/주소 검색: https://dapi.kakao.com/v2/local/search/keyword.json</li>
 *   <li>카테고리 검색: https://dapi.kakao.com/v2/local/search/category.json
 *       — 지하철역(SW8), 주차장(PK6)</li>
 *   <li>카카오 지도 링크 URL 생성: https://map.kakao.com/link/map/ (서버 아웃바운드 없음)</li>
 * </ul>
 *
 * <p>인증 방식: {@code Authorization: KakaoAK {REST_API_KEY}}
 * REST API 키는 카카오 OAuth에 사용하는 {@code KAKAO_CLIENT_ID}와 동일하다.</p>
 */
@Slf4j
@Component
public class KakaoLocalClient {

    /** 카카오 키워드 검색 API URL */
    private static final String KAKAO_KEYWORD_SEARCH_URL = "https://dapi.kakao.com/v2/local/search/keyword.json";

    /** 카카오 카테고리 검색 API URL (좌표 기반 주변 장소 조회) */
    private static final String KAKAO_CATEGORY_SEARCH_URL = "https://dapi.kakao.com/v2/local/search/category.json";

    /** 지하철역 카테고리 코드 */
    public static final String CATEGORY_SUBWAY = "SW8";

    /** 주차장 카테고리 코드 */
    public static final String CATEGORY_PARKING = "PK6";

    /** 검색 결과 기본 반환 건수 */
    private static final int DEFAULT_SEARCH_SIZE = 10;

    @Value("${kakao.client.id}")
    private String kakaoRestApiKey;

    private final RestClient restClient;

    public KakaoLocalClient() {
        this.restClient = RestClient.create();
    }

    /**
     * 장소명 또는 주소 키워드로 카카오 검색
     *
     * @param query 검색 키워드
     * @param page  결과 페이지 번호 (1~45)
     * @param size  페이지당 결과 수 (1~15)
     * @return 카카오 키워드 검색 원시 응답
     */
    public KakaoAddressResponseDto searchAddress(String query, int page, int size) {
        String uri = UriComponentsBuilder.fromUriString(KAKAO_KEYWORD_SEARCH_URL)
            .queryParam("query", query)
            .queryParam("page", page)
            .queryParam("size", size)
            .build()
            .toUriString();

        try {
            RestClient.ResponseSpec responseSpec = restClient.get()
                .uri(uri)
                .header("Authorization", "KakaoAK " + kakaoRestApiKey)
                .retrieve();

            KakaoAddressResponseDto response = responseSpec.body(KakaoAddressResponseDto.class);
            if (response == null) {
                throw new RuntimeException("카카오 키워드 검색 결과를 받지 못했습니다.");
            }
            return response;

        } catch (RestClientException e) {
            throw new RuntimeException("카카오 키워드 검색 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 좌표 기반 카카오 카테고리 검색
     *
     * <p>지정한 좌표를 중심으로 반경 내의 특정 카테고리 장소를 검색한다.
     * 교통수단 자동 추천 중 지하철역(SW8) 조회에 사용한다.</p>
     *
     * <p>카카오 좌표 파라미터 주의: x = 경도(lng), y = 위도(lat)</p>
     *
     * @param categoryGroupCode 카테고리 코드 (CATEGORY_SUBWAY)
     * @param lng               중심 경도 (longitude)
     * @param lat               중심 위도 (latitude)
     * @param radius            탐색 반경 (m, 카카오 최대 20000)
     * @return 카카오 카테고리 검색 원시 응답
     */
    public KakaoCategoryResponseDto searchByCategory(
            String categoryGroupCode, double lng, double lat, int radius) {

        String uri = UriComponentsBuilder.fromUriString(KAKAO_CATEGORY_SEARCH_URL)
            .queryParam("category_group_code", categoryGroupCode)
            .queryParam("x", lng)
            .queryParam("y", lat)
            .queryParam("radius", radius)
            .queryParam("sort", "distance")
            .build()
            .toUriString();

        log.info("[KakaoLocalClient] 카테고리 검색 요청: category={}, lat={}, lng={}, radius={}",
            categoryGroupCode, lat, lng, radius);

        try {
            RestClient.ResponseSpec responseSpec = restClient.get()
                .uri(uri)
                .header("Authorization", "KakaoAK " + kakaoRestApiKey)
                .retrieve();

            KakaoCategoryResponseDto response = responseSpec.body(KakaoCategoryResponseDto.class);
            if (response == null) {
                throw new RuntimeException("카카오 카테고리 검색 결과를 받지 못했습니다.");
            }

            log.info("[KakaoLocalClient] 카테고리 검색 완료: category={}, 결과={}건",
                categoryGroupCode,
                response.getDocuments() != null ? response.getDocuments().size() : 0);

            return response;

        } catch (RestClientException e) {
            throw new RuntimeException("카카오 카테고리 검색 중 오류가 발생했습니다.", e);
        }
    }
}
