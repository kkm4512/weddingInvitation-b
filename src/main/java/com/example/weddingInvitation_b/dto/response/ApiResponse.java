package com.example.weddingInvitation_b.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * API 응답 래퍼 클래스
 *
 * <p>모든 API 응답을 일관된 형식으로 반환하기 위한 래퍼 클래스이다.
 * code, message, datas 필드로 구성되며, 성공/실패 여부와 데이터를 명확히 구분한다.</p>
 *
 * @param <T> 응답 데이터 타입
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {

    /** HTTP 상태 코드 */
    private int code;

    /** 응답 메시지 */
    private String message;

    /** 실제 응답 데이터 */
    private T datas;

    /**
     * 성공 응답 생성
     *
     * @param data 응답 데이터
     * @return 성공 ApiResponse 객체
     * @param <T> 데이터 타입
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
            .code(ApiResponseCode.SUCCESS.getCode())
            .message(ApiResponseCode.SUCCESS.getMessage())
            .datas(data)
            .build();
    }

    /**
     * 성공 응답 생성 (데이터 없음)
     *
     * @return 성공 ApiResponse 객체 (datas는 null)
     */
    public static ApiResponse<Void> success() {
        return ApiResponse.<Void>builder()
            .code(ApiResponseCode.SUCCESS.getCode())
            .message(ApiResponseCode.SUCCESS.getMessage())
            .datas(null)
            .build();
    }
}
