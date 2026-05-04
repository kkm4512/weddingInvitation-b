package com.example.weddingInvitation_b.dto.response;

/**
 * 로그인 응답 DTO
 *
 * <p>카카오 OAuth 로그인 성공 시 클라이언트에게 반환하는 인증 정보다.
 * 클라이언트는 accessToken을 저장하여 이후 API 요청 시
 * Authorization: Bearer {accessToken} 헤더에 포함해야 한다.</p>
 *
 * @param userId      서버 자체 사용자 ID (AUTO_INCREMENT)
 * @param accessToken 자체 발급 JWT (HS256, 7일 만료)
 */
public record LoginResponseDto(
    Long userId,
    String accessToken
) {

    /**
     * LoginResponseDto 생성
     *
     * @param userId      사용자 ID
     * @param accessToken JWT 문자열
     * @return LoginResponseDto 인스턴스
     */
    public static LoginResponseDto of(Long userId, String accessToken) {
        return new LoginResponseDto(userId, accessToken);
    }
}
