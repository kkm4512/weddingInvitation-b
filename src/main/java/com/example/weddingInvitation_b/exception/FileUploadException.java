package com.example.weddingInvitation_b.exception;

/**
 * 파일 업로드 실패 시 발생하는 예외
 * 
 * <p>S3 또는 Cloudflare로의 파일 업로드 중 오류가 발생한 경우에 사용된다.</p>
 */
public class FileUploadException extends RuntimeException {
    
    public FileUploadException(String message) {
        super(message);
    }

    public FileUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}

