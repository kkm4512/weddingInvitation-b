package com.example.weddingInvitation_b.dto.response;

/**
 * API 응답 코드 및 메시지 enum
 *
 * <p>모든 API 응답에서 일관된 코드와 메시지를 사용하기 위해 정의한다.
 * 현재는 성공(200) 케이스만 정의되어 있으며, 추후 확장 가능하다.</p>
 */
public enum ApiResponseCode {

    /** 성공 */
    SUCCESS(200, "Success");

    private final int code;
    private final String message;

    ApiResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
