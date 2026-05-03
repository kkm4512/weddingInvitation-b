package com.example.weddingInvitation_b.service.impl;

import com.example.weddingInvitation_b.domain.User;
import com.example.weddingInvitation_b.dto.response.UserResponseDto;
import com.example.weddingInvitation_b.exception.EntityNotFoundException;
import com.example.weddingInvitation_b.repository.UserRepository;
import com.example.weddingInvitation_b.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 인증 서비스 구현체
 * 
 * <p>소셜 로그인 처리 및 사용자 정보 관리를 담당한다.
 * 앞으로 실제 OAuth 클라이언트 구현이 추가될 예정이다.</p>
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    /**
     * 현재 로그인한 사용자 정보 조회
     * 
     * @param userId 사용자 ID
     * @return 사용자 정보
     * @throws EntityNotFoundException 사용자가 존재하지 않을 경우
     */
    @Override
    public UserResponseDto getCurrentUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. userId=" + userId));
        return UserResponseDto.from(user);
    }

    /**
     * 소셜 로그인 처리
     * 
     * <p>제공자별 사용자 정보로 로그인하며, 기존 사용자면 정보를 업데이트하고,
     * 신규 사용자면 새로 생성한다.</p>
     * 
     * @param provider 소셜 제공자 (kakao, google, naver)
     * @param providerId 소셜 제공자의 사용자 ID
     * @param name 사용자 이름
     * @param email 사용자 이메일
     * @return 로그인한 사용자 정보
     */
    @Override
    @Transactional
    public UserResponseDto loginWithProvider(String provider, String providerId, String name, String email) {
        // 기존 사용자 확인
        User user = userRepository.findByProviderId(providerId)
            .orElseGet(() -> {
                // 신규 사용자 생성
                User newUser = User.builder()
                    .provider(provider)
                    .providerId(providerId)
                    .name(name)
                    .email(email)
                    .profileImageUrl(null)  // 추후 OAuth 응답에서 추출
                    .isDeleted(false)
                    .build();
                return userRepository.save(newUser);
            });

        return UserResponseDto.from(user);
    }

    /**
     * 로그아웃 처리
     * 
     * <p>카카오 OAuth 세션을 무효화한다.
     * 실제 구현 시 Spring Security의 SecurityContextHolder를 통해 세션을 종료한다.</p>
     *
     * @param userId 로그아웃하는 사용자 ID
     */
    @Override
    @Transactional
    public void logout(Long userId) {
        // 샘플 구현: 실제로는 Spring Security OAuth2 세션 무효화로 처리
        // SecurityContextHolder.clearContext() 또는 HttpSession.invalidate() 사용
    }
}

