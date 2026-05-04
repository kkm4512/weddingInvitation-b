package com.example.weddingInvitation_b.controller;

import com.example.weddingInvitation_b.client.KakaoOAuthClient;
import com.example.weddingInvitation_b.dto.response.ApiResponse;
import com.example.weddingInvitation_b.dto.response.LoginResponseDto;
import com.example.weddingInvitation_b.dto.response.UserResponseDto;
import com.example.weddingInvitation_b.service.AuthService;
import com.example.weddingInvitation_b.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 인증 관련 API 컨트롤러
 *
 * <p>카카오 OAuth 2.0 로그인 흐름을 처리한다.
 * Kakao API 통신은 KakaoOAuthClient에, 비즈니스 로직은 AuthService에 위임한다.</p>
 *
 * <p>로그인 흐름:
 * <ol>
 *   <li>GET /kakao -&gt; 카카오 인증 URL을 JSON으로 반환 (프론트가 window.location.href로 이동)</li>
 *   <li>카카오 로그인 완료 -&gt; 브라우저가 GET /kakao/callback?code=xxx 로 이동 (서버 진입)</li>
 *   <li>GET /kakao/callback -&gt; 토큰 교환 -&gt; 사용자 조회/생성 -&gt; JWT 발급 -&gt; JSON 반환</li>
 * </ol>
 * </p>
 *
 * <p>이후 인증 흐름:
 * 클라이언트는 응답받은 accessToken을 저장하고,
 * 이후 모든 API 요청 시 Authorization: Bearer {accessToken} 헤더에 포함한다.</p>
 *
 * @see AuthService
 * @see KakaoOAuthClient
 * @see JwtProvider
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final KakaoOAuthClient kakaoOAuthClient;
    private final JwtProvider jwtProvider;

    /**
     * 카카오 OAuth 로그인 URL 반환
     *
     * <p>React(프론트엔드)가 이 URL을 받아 window.location.href로 직접 이동한다.
     * 서버가 redirect를 처리하지 않으므로 CORS 문제가 발생하지 않는다.</p>
     *
     * <p>프론트엔드 사용 예시:
     * <pre>
     * const { data } = await axios.get('/api/v1/auth/kakao');
     * window.location.href = data.datas.redirectUrl;
     * </pre>
     * </p>
     *
     * @return 카카오 인증 페이지 URL
     */
    @GetMapping("/kakao")
    public ApiResponse<Map<String, String>> kakaoLogin() {
        String kakaoAuthUrl = kakaoOAuthClient.getAuthorizationUrl();
        return ApiResponse.success(Map.of("redirectUrl", kakaoAuthUrl));
    }

    /**
     * 카카오 OAuth 콜백 처리 및 JWT 발급
     *
     * <p>카카오 인증 서버에서 전달된 인가 코드로 아래 작업을 순서대로 처리한다:
     * <ol>
     *   <li>AuthService.loginWithKakao(code) 호출 -&gt; 액세스 토큰 교환 -&gt; 사용자 DB upsert</li>
     *   <li>JwtProvider로 자체 JWT 생성</li>
     *   <li>userId + accessToken을 JSON으로 반환</li>
     * </ol>
     * </p>
     *
     * @param code 카카오 인증 서버에서 받은 인가 코드
     * @return userId + accessToken
     */
    @GetMapping("/kakao/callback")
    public ApiResponse<LoginResponseDto> kakaoCallback(@RequestParam String code) {
        // 1. 인가 코드로 로그인 처리 (토큰 교환 -> 사용자 조회/생성)
        UserResponseDto user = authService.loginWithKakao(code);

        log.debug("kakao login success - userId: {}", user.getUserId());

        // 2. 자체 JWT 생성 (HS256, userId payload, 7일 만료)
        String jwt = jwtProvider.generateToken(user.getUserId());

        // 3. userId + accessToken 반환
        return ApiResponse.success(LoginResponseDto.of(user.getUserId(), jwt));
    }

    /**
     * 현재 로그인 사용자 정보 조회
     *
     * <p>JwtAuthenticationFilter가 SecurityContext에 저장한 userId를 추출하여
     * 사용자 정보를 반환한다.</p>
     *
     * @return 현재 사용자 정보
     */
    @GetMapping("/me")
    public ApiResponse<UserResponseDto> getCurrentUser() {
        // JwtAuthenticationFilter가 principal에 userId(Long)를 저장
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        UserResponseDto user = authService.getCurrentUser(userId);
        return ApiResponse.success(user);
    }

    /**
     * 로그아웃 처리
     *
     * <p>JWT는 Stateless이므로 서버에서 별도로 무효화할 상태가 없다.
     * 클라이언트가 저장된 accessToken을 직접 삭제하면 로그아웃이 완료된다.</p>
     *
     * @return 로그아웃 결과
     */
    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        // JWT Stateless: 클라이언트가 토큰을 삭제하면 로그아웃 완료
        // 추후 토큰 블랙리스트 등 서버사이드 무효화 로직 추가 가능
        return ApiResponse.success();
    }
}
