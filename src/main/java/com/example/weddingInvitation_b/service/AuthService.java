package com.example.weddingInvitation_b.service;

import com.example.weddingInvitation_b.dto.response.UserResponseDto;

/**
 * 인증 서비스 인터페이스
 */
public interface AuthService {

    /**
     * 카카오 인가 코드로 로그인 처리
     *
     * <p>인가 코드 → 액세스 토큰 교환 → 사용자 정보 조회 → DB upsert 순으로 처리한다.</p>
     *
     * @param code 카카오 인증 서버에서 받은 인가 코드
     * @return 로그인한 사용자 정보
     */
    UserResponseDto loginWithKakao(String code);

    /**
     * 소셜 로그인 공통 처리 (신규 생성 또는 기존 사용자 반환)
     *
     * @param provider   소셜 제공자 (kakao, google, naver)
     * @param providerId 소셜 제공자의 사용자 ID
     * @param name       사용자 이름
     * @param email      사용자 이메일
     * @return 사용자 정보
     */
    UserResponseDto loginWithProvider(String provider, String providerId, String name, String email);

    /**
     * 현재 로그인한 사용자 정보 조회
     *
     * @param userId 사용자 ID
     * @return 사용자 정보
     */
    UserResponseDto getCurrentUser(Long userId);
}

