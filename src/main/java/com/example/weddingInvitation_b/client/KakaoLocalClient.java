package com.example.weddingInvitation_b.client;

import com.example.weddingInvitation_b.dto.response.KakaoAddressResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 카카오 로컬 API 통신 클라이언트
 *
 * <p>예식 장소 편집에 필요한 두 가지 기능을 전담한다.</p>
 *
 * <ul>
 *   <li>장소/주소 검색: https://dapi.kakao.com/v2/local/search/keyword.json</li>
 *   <li>카카오 지도 링크 URL 생성: https://map.kakao.com/link/map/ (서버 아웃바운드 없음)</li>
 * </ul>
 *
 * <p>인증 방식: {@code Authorization: KakaoAK {REST_API_KEY}}
 * REST API 키는 카카오 OAuth에 사용하는 {@code KAKAO_CLIENT_ID}와 동일하다.</p>
 */
@Slf4j
@Component
public class KakaoLocalClient {

    /** 카카오 키워드 검색 API URL (장소명/주소 모두 검색 가능) */
    private static final String KAKAO_KEYWORD_SEARCH_URL = "https://dapi.kakao.com/v2/local/search/keyword.json";

    /** 검색 결과 최대 반환 건수 */
    private static final int SEARCH_SIZE = 10;

    @Value("${kakao.client.id}")
    private String kakaoRestApiKey;

    private final RestClient restClient;

    public KakaoLocalClient() {
        this.restClient = RestClient.create();
    }

    /**
     * 장소명 또는 주소 키워드로 카카오 검색
     *
     * <p>카카오 키워드 검색 API를 호출한다.
     * 주소 검색 API와 달리 "역삼역", "그랜드볼룸" 같은 장소명도 검색된다.</p>
     *
     * <p>URI는 UriComponentsBuilder로 문자열로 만든 뒤 RestClient에 전달하여
     * 타입 추론 오류를 방지한다.</p>
     *
     * @param query 검색 키워드 (예: "역삼역", "강남구 테헤란로")
     * @return 카카오 키워드 검색 원시 응답 (최대 {@value #SEARCH_SIZE}건)
     * @throws RuntimeException 카카오 API 호출 실패 시
     */
    public KakaoAddressResponseDto searchAddress(String query) {
        // URI를 문자열로 먼저 구성 → RestClient 타입 추론 오류 방지
        String uri = UriComponentsBuilder.fromUriString(KAKAO_KEYWORD_SEARCH_URL)
            .queryParam("query", query)
            .queryParam("size", SEARCH_SIZE)
            .build()
            .toUriString();

        try {
            // retrieve() 이후에 변수를 끊음 — ResponseSpec은 와일드카드 없이 명확한 타입
            RestClient.ResponseSpec responseSpec = restClient.get()
                .uri(uri)
                .header("Authorization", "KakaoAK " + kakaoRestApiKey)
                .retrieve();

            KakaoAddressResponseDto response = responseSpec
                .body(KakaoAddressResponseDto.class);

            if (response == null) {
                throw new RuntimeException("카카오 키워드 검색 결과를 받지 못했습니다.");
            }
            return response;

        } catch (RestClientException e) {
            throw new RuntimeException("카카오 키워드 검색 중 오류가 발생했습니다.", e);
        }
    }

}
