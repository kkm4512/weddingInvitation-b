package com.example.weddingInvitation_b.service.impl;

import lombok.extern.slf4j.Slf4j;
import com.example.weddingInvitation_b.client.KakaoOAuthClient;
import com.example.weddingInvitation_b.domain.User;
import com.example.weddingInvitation_b.dto.response.KakaoTokenResponseDto;
import com.example.weddingInvitation_b.dto.response.KakaoUserInfoResponseDto;
import com.example.weddingInvitation_b.dto.response.UserResponseDto;
import com.example.weddingInvitation_b.exception.EntityNotFoundException;
import com.example.weddingInvitation_b.repository.UserRepository;
import com.example.weddingInvitation_b.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 인증 서비스 구현체
 *
 * <p>카카오 OAuth 로그인 처리 및 사용자 정보 관리를 담당한다.
 * Kakao API 통신은 KakaoOAuthClient에 위임하고,
 * 이 클래스는 사용자 DB 조회/저장 비즈니스 로직에만 집중한다.</p>
 *
 * @see KakaoOAuthClient
 * @see UserRepository
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final KakaoOAuthClient kakaoOAuthClient;

    /**
     * 카카오 인가 코드로 로그인 처리
     *
     * <p>아래 순서로 처리한다:
     * <ol>
     *   <li>인가 코드 → 카카오 액세스 토큰 교환</li>
     *   <li>액세스 토큰 → 카카오 사용자 정보 조회</li>
     *   <li>사용자 정보로 DB upsert (신규 생성 or 기존 반환)</li>
     * </ol>
     * </p>
     *
     * @param code 카카오 인증 서버에서 받은 인가 코드
     * @return 로그인한 사용자 정보
     */
    @Override
    @Transactional
    public UserResponseDto loginWithKakao(String code) {
        // 1. 인가 코드 → 카카오 액세스 토큰 교환
        KakaoTokenResponseDto token = kakaoOAuthClient.getAccessToken(code);

        // 2. 액세스 토큰 → 카카오 사용자 정보 조회
        KakaoUserInfoResponseDto userInfo = kakaoOAuthClient.getUserInfo(token.getAccessToken());

        log.debug("userInfo {}", userInfo);

        // 3. 카카오 사용자 정보로 DB upsert
        return loginWithProvider(
            "kakao",
            String.valueOf(userInfo.getId()),
            userInfo.getNickname(),
            userInfo.getEmail()
        );
    }

    /**
     * 소셜 로그인 공통 처리 (신규 생성 또는 기존 사용자 반환)
     *
     * <p>providerId로 기존 사용자를 조회하고, 없으면 새로 생성한다.
     * 기존 사용자라도 이름/이메일은 최신 값으로 갱신하지 않는다 (의도된 동작).</p>
     *
     * @param provider   소셜 제공자 (kakao, google, naver)
     * @param providerId 소셜 제공자의 사용자 ID
     * @param name       사용자 이름
     * @param email      사용자 이메일
     * @return 사용자 정보
     */
    @Override
    @Transactional
    public UserResponseDto loginWithProvider(String provider, String providerId, String name, String email) {
        // providerId로 기존 사용자 확인 → 없으면 신규 생성
        User user = userRepository.findByProviderId(providerId)
            .orElseGet(() -> {
                User newUser = User.builder()
                    .provider(provider)
                    .providerId(providerId)
                    .name(name)
                    .email(email)
                    .isDeleted(false)
                    .build();
                return userRepository.save(newUser);
            });

        return UserResponseDto.from(user);
    }

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
     * 로그아웃 처리
     *
     * <p>JWT 기반 Stateless 인증이므로 서버에서 별도로 무효화할 상태가 없다.
     * 쿠키 삭제(access_token MaxAge=0)는 Controller에서 처리한다.
     * 추후 카카오 토큰 만료 API 호출 등이 필요하면 여기에 추가한다.</p>
     *
     * @param userId 로그아웃하는 사용자 ID
     */
    @Override
    public void logout(Long userId) {
        // JWT Stateless: 서버 상태 없음 (쿠키 삭제는 Controller에서 처리)
        // 추후 카카오 토큰 만료 API 호출 등 서버사이드 로그아웃 로직 추가 가능
    }
}
