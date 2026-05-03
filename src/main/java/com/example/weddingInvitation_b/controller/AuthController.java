package com.example.weddingInvitation_b.controller;

import com.example.weddingInvitation_b.dto.response.ApiResponse;
import com.example.weddingInvitation_b.dto.response.UserResponseDto;
import com.example.weddingInvitation_b.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 인증 관련 API 컨트롤러
 * 
 * <p>소셜 로그인, 로그아웃, 사용자 정보 조회 등의 요청을 처리한다.
 * 실제 OAuth 클라이언트 로직은 추후 구현될 예정이다.</p>
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 카카오 OAuth 로그인 시작 (샘플)
     * 
     * <p>실제 구현 시 카카오 인증 서버로 리다이렉트해야 한다.</p>
     * 
     * @return 로그인 상태
     */
    @GetMapping("/kakao")
    public ApiResponse<String> kakaoLogin() {
        // 샘플 구현: 실제로는 카카오 인증 URL로 리다이렉트
        return ApiResponse.success("카카오 로그인 페이지로 리다이렉트 (미구현)");
    }

    /**
     * 카카오 OAuth 콜백 처리 (샘플)
     * 
     * <p>카카오 인증 서버에서 인가 코드를 받아 처리한다.</p>
     * 
     * @param code 카카오 인가 코드
     * @return 로그인 사용자 정보 (Spring Security 세션 발급)
     */
    @GetMapping("/kakao/callback")
    public ApiResponse<String> kakaoCallback(@RequestParam String code) {
        // 샘플 구현: 실제로는 Spring Security OAuth2 Client가 자동 처리 (카카오 액세스 토큰 요청 → 사용자 정보 조회 → 세션 발급)
        return ApiResponse.success("카카오 로그인 완료 및 세션 발급 (미구현, 인가 코드: " + code + ")");
    }

    /**
     * 현재 로그인 사용자 정보 조회
     * 
     * <p>Spring Security 세션(SecurityContext)에서 인증된 사용자 정보를 반환한다.</p>
     *
     * @return 현재 사용자 정보
     */
    @GetMapping("/me")
    public ApiResponse<UserResponseDto> getCurrentUser() {
        // 샘플 구현: 실제로는 SecurityContextHolder에서 인증된 사용자 ID를 추출
        Long sampleUserId = 1L; // 샘플 사용자 ID
        UserResponseDto user = authService.getCurrentUser(sampleUserId);
        return ApiResponse.success(user);
    }

    /**
     * 로그아웃 처리
     * 
     * <p>Spring Security 세션을 무효화하여 로그아웃 처리한다.</p>
     *
     * @return 로그아웃 결과
     */
    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        // 샘플 구현: 실제로는 SecurityContextHolder에서 인증된 사용자 ID를 추출
        Long sampleUserId = 1L; // 샘플 사용자 ID
        authService.logout(sampleUserId);
        return ApiResponse.success();
    }
}
