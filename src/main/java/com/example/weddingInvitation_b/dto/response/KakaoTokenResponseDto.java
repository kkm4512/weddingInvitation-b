package com.example.weddingInvitation_b.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 카카오 OAuth 토큰 응답 DTO
 *
 * <p>카카오 토큰 엔드포인트(https://kauth.kakao.com/oauth/token)에서
 * 반환되는 액세스 토큰 정보를 담는다.</p>
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoTokenResponseDto {

    /** 카카오 API 호출에 사용하는 액세스 토큰 */
    @JsonProperty("access_token")
    private String accessToken;

    /** 토큰 타입 (Bearer) */
    @JsonProperty("token_type")
    private String tokenType;

    /** 액세스 토큰 만료 시간 (초) */
    @JsonProperty("expires_in")
    private Long expiresIn;

    /** 액세스 토큰 갱신에 사용하는 리프레시 토큰 */
    @JsonProperty("refresh_token")
    private String refreshToken;
}
