package com.example.weddingInvitation_b.exception;

/**
 * 요청한 엔티티를 찾을 수 없을 때 발생하는 예외 (Not Found: 404)
 * 
 * <p>청첩장, 사용자, 갤러리 사진 등 데이터베이스에서 조회 시 찾을 수 없는 경우 사용된다.</p>
 */
public class EntityNotFoundException extends RuntimeException {
    
    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

