package com.example.weddingInvitation_b.exception;

import lombok.Data;

/**
 * API 에러 응답 DTO
 * 
 * <p>모든 API 예외 발생 시 이 형식으로 클라이언트에 반환된다.</p>
 */
@Data
public class ErrorResponse {
    private int status;
    private String message;
    private String error;
    private long timestamp;

    public ErrorResponse(int status, String message, String error) {
        this.status = status;
        this.message = message;
        this.error = error;
        this.timestamp = System.currentTimeMillis();
    }
}

