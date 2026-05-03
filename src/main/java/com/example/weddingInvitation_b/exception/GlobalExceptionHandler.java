package com.example.weddingInvitation_b.exception;

import com.example.weddingInvitation_b.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 전역 예외 핸들러
 * 
 * <p>모든 Controller에서 발생하는 예외를 중앙에서 처리하고,
 * 일관된 형식으로 클라이언트에 에러 응답을 반환한다.</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 엔티티를 찾을 수 없는 경우 (404 Not Found)
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleEntityNotFound(EntityNotFoundException ex) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
            .code(HttpStatus.NOT_FOUND.value())
            .message(ex.getMessage())
            .datas(null)
            .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * 인증 실패 시 (401 Unauthorized)
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationException(AuthenticationException ex) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
            .code(HttpStatus.UNAUTHORIZED.value())
            .message(ex.getMessage())
            .datas(null)
            .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * 권한 없음 (403 Forbidden)
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException ex) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
            .code(HttpStatus.FORBIDDEN.value())
            .message(ex.getMessage())
            .datas(null)
            .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    /**
     * 파일 업로드 실패 (400 Bad Request)
     */
    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ApiResponse<Void>> handleFileUploadException(FileUploadException ex) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
            .code(HttpStatus.BAD_REQUEST.value())
            .message(ex.getMessage())
            .datas(null)
            .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 잘못된 요청 (400 Bad Request)
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequestException(BadRequestException ex) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
            .code(HttpStatus.BAD_REQUEST.value())
            .message(ex.getMessage())
            .datas(null)
            .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 요청 데이터 검증 실패 (400 Bad Request)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .reduce((a, b) -> a + ", " + b)
            .orElse("Validation failed");
        
        ApiResponse<Void> response = ApiResponse.<Void>builder()
            .code(HttpStatus.BAD_REQUEST.value())
            .message(message)
            .datas(null)
            .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 예상치 못한 예외 (500 Internal Server Error)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .message("An unexpected error occurred")
            .datas(null)
            .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
