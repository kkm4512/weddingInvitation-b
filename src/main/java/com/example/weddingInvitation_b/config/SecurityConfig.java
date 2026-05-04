package com.example.weddingInvitation_b.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Spring Security 및 CORS 설정
 *
 * <p>JWT 기반 Stateless 인증을 적용한다.
 * 세션을 사용하지 않으며, 모든 요청은 JwtAuthenticationFilter에서 검증된다.</p>
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Security 필터 체인 설정
     *
     * <p>JWT Stateless 인증 적용:
     * <ul>
     *   <li>세션 생성 정책: STATELESS (서버가 세션을 생성하거나 사용하지 않음)</li>
     *   <li>JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 앞에 등록</li>
     *   <li>공개 엔드포인트는 permitAll, 나머지는 인증 필요</li>
     * </ul>
     * </p>
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CORS 설정 적용
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // REST API 서버이므로 CSRF 비활성화
            .csrf(AbstractHttpConfigurer::disable)

            // JWT Stateless 인증 - 세션 생성/사용 안 함
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // 엔드포인트별 인가 규칙
            .authorizeHttpRequests(auth -> auth

                // 헬스 체크 - 인증 불필요 (Docker, Nginx, CI/CD 공통 사용)
                .requestMatchers("/health").permitAll()

                // 인증 관련 - 인증 불필요
                .requestMatchers("/api/v1/auth/**").permitAll()

                // 하객용 공개 청첩장 뷰 - 인증 불필요
                .requestMatchers("/api/v1/w/**").permitAll()
                .requestMatchers("/api/v1/mcards/preview/**").permitAll()

                // 정적 목록 조회 - 인증 불필요
                .requestMatchers(HttpMethod.GET, "/api/v1/intros").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/themes").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/themes/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/greetings/samples").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/quotes/samples").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/notices/samples").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/bgm/samples").permitAll()

                // 하객 RSVP 제출, 방명록 작성 - 인증 불필요
                .requestMatchers(HttpMethod.POST, "/api/v1/mcards/*/rsvp").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/mcards/*/guestbook").permitAll()

                // 나머지 모든 요청 - 인증 필요
                .anyRequest().authenticated()
            )

            // 기본 formLogin / httpBasic 비활성화 (REST API 서버)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)

            // JwtAuthenticationFilter 등록 (UsernamePasswordAuthenticationFilter 앞에 실행)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * CORS 허용 정책
     *
     * <p>프론트엔드 개발 서버(React)로부터의 요청을 허용한다.
     * 쿠키(JWT) 전송을 위해 allowCredentials(true) 적용.
     * 운영 환경에서는 allowedOrigins를 실제 도메인으로 교체해야 한다.</p>
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // 허용할 Origin (프론트엔드 개발 서버)
        config.setAllowedOrigins(List.of(
            "http://localhost:3000",   // React (CRA)
            "http://localhost:5173"    // React (Vite)
        ));

        // 허용할 HTTP 메서드
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        // 허용할 헤더
        config.setAllowedHeaders(List.of("*"));

        // JWT 쿠키 포함 요청 허용 (credentials: true)
        config.setAllowCredentials(true);

        // Preflight 캐시 시간 (초)
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
