package com.example.weddingInvitation_b.exception;

/**
 * 잘못된 요청 시 발생하는 예외 (Bad Request: 400)
 * 
 * <p>입력 데이터 검증 실패, 비즈니스 로직 위반 등의 클라이언트 오류에 사용된다.</p>
 */
public class BadRequestException extends RuntimeException {
    
    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}

