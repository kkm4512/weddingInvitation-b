package com.example.weddingInvitation_b.exception;

/**
 * 권한 없음 시 발생하는 예외 (Forbidden: 403)
 * 
 * <p>인증된 사용자이지만, 특정 리소스에 접근할 권한이 없는 경우에 사용된다.</p>
 */
public class AccessDeniedException extends RuntimeException {
    
    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}

