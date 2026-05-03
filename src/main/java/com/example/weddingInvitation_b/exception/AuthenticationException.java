package com.example.weddingInvitation_b.exception;

/**
 * 인증 실패 시 발생하는 예외 (Unauthorized: 401)
 * 
 * <p>JWT 토큰 검증 실패, 로그인 필요 등의 인증 관련 오류에 사용된다.</p>
 */
public class AuthenticationException extends RuntimeException {
    
    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}

