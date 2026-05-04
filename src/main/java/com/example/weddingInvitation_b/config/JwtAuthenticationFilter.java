package com.example.weddingInvitation_b.config;

import com.example.weddingInvitation_b.util.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * JWT 인증 필터
 *
 * <p>모든 요청의 Authorization 헤더에서 Bearer JWT를 추출하고 검증한다.
 * 유효한 JWT가 있으면 SecurityContext에 Authentication을 설정한다.</p>
 *
 * <p>필터 처리 흐름:
 * <pre>
 * 요청 수신 -> Authorization 헤더에서 Bearer 토큰 추출
 *           -> JWT 유효성 검증 (서명 + 만료)
 *           -> userId 추출 -> SecurityContext에 Authentication 저장
 *           -> 다음 필터로 전달
 * </pre>
 * JWT가 없거나 유효하지 않으면 SecurityContext를 설정하지 않고 통과시킨다.
 * 이후 인증이 필요한 엔드포인트는 Spring Security가 401로 차단한다.</p>
 *
 * @see JwtProvider
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /** Authorization 헤더 이름 */
    private static final String AUTHORIZATION_HEADER = "Authorization";

    /** Bearer 토큰 접두사 */
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtProvider jwtProvider;

    /**
     * 요청별 JWT 검증 및 SecurityContext 인증 설정
     *K
     * )
     *
     *
     *
     *
     *
     *
     *
     *
     * Collections
     *
     * Environments
     *
     * Specs
     *
     * Flows
     * @param request     HTTP 요청
     * @param response    HTTP 응답
     * @param filterChain 다음 필터 체인
     * @throws ServletException 필터 처리 오류
     * @throws IOException      IO 오류
     */
    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Authorization 헤더에서 Bearer 토큰 추출
        String token = extractBearerToken(request);

        // 2. JWT 검증 및 SecurityContext 설정
        if (token != null && jwtProvider.validateToken(token)) {
            Long userId = jwtProvider.getUserId(token);

            // principal에 userId 저장 -> Controller에서 SecurityContextHolder로 조회
            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userId, null, List.of());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.debug("JWT 인증 성공 - userId: {}", userId);
        }

        // 3. 다음 필터로 전달
        filterChain.doFilter(request, response);
    }

    /**
     * Authorization 헤더에서 Bearer 토큰 추출
     *
     * @param request HTTP 요청
     * @return JWT 문자열 (헤더가 없거나 형식이 맞지 않으면 null)
     */
    private String extractBearerToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(authHeader) && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(BEARER_PREFIX.length());
        }

        return null;
    }
}
