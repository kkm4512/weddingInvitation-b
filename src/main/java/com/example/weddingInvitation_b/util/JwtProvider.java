package com.example.weddingInvitation_b.util;

import com.example.weddingInvitation_b.exception.AuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 생성 및 검증 유틸리티
 *
 * <p>카카오 로그인 성공 후 발급하는 자체 JWT를 관리한다.
 * 알고리즘: HS256 / payload: userId / 만료: 7일</p>
 *
 * <p>JWT 흐름:
 * <pre>
 * 로그인 성공 → generateToken(userId) → HttpOnly Cookie 저장
 * 요청 수신  → validateToken(token)   → 서명/만료 검증
 * 인증 처리  → getUserId(token)       → SecurityContext에 userId 저장
 * </pre>
 * </p>
 */
@Component
public class JwtProvider {

    /** JWT 만료 시간: 7일 (밀리초) */
    private static final long EXPIRATION_MS = 7 * 24 * 60 * 60 * 1000L;

    /** JWT 서명에 사용하는 비밀키 (HS256, 최소 32자 이상) */
    @Value("${jwt.secret}")
    private String secretKey;

    /**
     * userId를 payload에 담아 JWT 생성
     *
     * @param userId 토큰 주체 (사용자 ID)
     * @return 서명된 JWT 문자열
     */
    public String generateToken(Long userId) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_MS);

        return Jwts.builder()
            .subject(String.valueOf(userId))
            .issuedAt(now)
            .expiration(expiration)
            .signWith(getSigningKey())
            .compact();
    }

    /**
     * JWT 유효성 검증 (서명 + 만료 시간)
     *
     * @param token 검증할 JWT 문자열
     * @return 유효하면 true, 서명 불일치·만료·형식 오류면 false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * JWT payload에서 userId 추출
     *
     * @param token 유효한 JWT 문자열
     * @return 토큰에 담긴 사용자 ID
     * @throws AuthenticationException 토큰 파싱 실패 시
     */
    public Long getUserId(String token) {
        try {
            Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
            return Long.parseLong(claims.getSubject());
        } catch (JwtException | IllegalArgumentException e) {
            throw new AuthenticationException("JWT 파싱에 실패했습니다.", e);
        }
    }

    /**
     * secretKey 문자열로 HS256 서명 키 생성
     *
     * @return HMAC-SHA256 서명 키
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
