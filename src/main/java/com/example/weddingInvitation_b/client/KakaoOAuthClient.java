package com.example.weddingInvitation_b.client;

import com.example.weddingInvitation_b.dto.response.KakaoTokenResponseDto;
import com.example.weddingInvitation_b.dto.response.KakaoUserInfoResponseDto;
import com.example.weddingInvitation_b.exception.AuthenticationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/**
 * 카카오 OAuth API 통신 클라이언트
 *
 * <p>카카오 인증 서버와의 HTTP 통신을 전담한다.
 * 액세스 토큰 발급 및 사용자 정보 조회 API를 호출한다.</p>
 *
 * <p>호출 흐름:
 * <pre>
 * 1. getAuthorizationUrl() -&gt; 카카오 로그인 페이지 URL 생성
 * 2. getAccessToken(code)  -&gt; 인가 코드를 액세스 토큰으로 교환
 * 3. getUserInfo(token)    -&gt; 액세스 토큰으로 사용자 정보 조회
 * </pre>
 * </p>
 *
 * @see KakaoTokenResponseDto
 * @see KakaoUserInfoResponseDto
 */
@Component
public class KakaoOAuthClient {

    /** 카카오 액세스 토큰 발급 URL */
    private static final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";

    /** 카카오 사용자 정보 조회 URL */
    private static final String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    /** 카카오 인증 페이지 기본 URL */
    private static final String KAKAO_AUTH_BASE_URL = "https://kauth.kakao.com/oauth/authorize";

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.client.secret}")
    private String clientSecret;

    @Value("${kakao.redirect.uri}")
    private String redirectUri;

    private final RestClient restClient;

    public KakaoOAuthClient() {
        this.restClient = RestClient.create();
    }

    /**
     * 카카오 로그인 인증 페이지 URL 생성
     *
     * <p>사용자를 카카오 로그인 페이지로 리다이렉트할 URL을 반환한다.</p>
     *
     * @return 카카오 인증 페이지 URL (response_type=code 포함)
     */
    public String getAuthorizationUrl() {
        return KAKAO_AUTH_BASE_URL
            + "?client_id=" + clientId
            + "&redirect_uri=" + redirectUri
            + "&response_type=code";
    }

    /**
     * 인가 코드를 카카오 액세스 토큰으로 교환
     *
     * <p>카카오 토큰 엔드포인트에 POST 요청을 보내 액세스 토큰을 발급받는다.</p>
     *
     * @param code 카카오 인증 서버에서 받은 인가 코드
     * @return 카카오 액세스 토큰 정보
     * @throws AuthenticationException 토큰 발급 요청 실패 시
     */
    public KakaoTokenResponseDto getAccessToken(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        try {
            // 타입 추론 오류 방지를 위해 RequestBodySpec을 명시적으로 분리
            RestClient.RequestBodySpec requestSpec = restClient.post()
                .uri(KAKAO_TOKEN_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(params);

            KakaoTokenResponseDto token = requestSpec
                .retrieve()
                .body(KakaoTokenResponseDto.class);

            if (token == null || token.getAccessToken() == null) {
                throw new AuthenticationException("카카오 액세스 토큰 발급에 실패했습니다.");
            }
            return token;

        } catch (RestClientException e) {
            throw new AuthenticationException("카카오 토큰 요청 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 카카오 액세스 토큰으로 사용자 정보 조회
     *
     * <p>카카오 사용자 정보 엔드포인트에 GET 요청을 보내
     * 카카오 계정의 이메일, 닉네임, 프로필 이미지를 조회한다.</p>
     *
     * @param accessToken 카카오 액세스 토큰
     * @return 카카오 사용자 정보 (id, email, nickname, profileImageUrl)
     * @throws AuthenticationException 사용자 정보 조회 실패 시
     */
    public KakaoUserInfoResponseDto getUserInfo(String accessToken) {
        try {
            KakaoUserInfoResponseDto userInfo = restClient.get()
                .uri(KAKAO_USER_INFO_URL)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(KakaoUserInfoResponseDto.class);

            if (userInfo == null || userInfo.getId() == null) {
                throw new AuthenticationException("카카오 사용자 정보 조회에 실패했습니다.");
            }
            return userInfo;

        } catch (RestClientException e) {
            throw new AuthenticationException("카카오 사용자 정보 요청 중 오류가 발생했습니다.", e);
        }
    }
}
