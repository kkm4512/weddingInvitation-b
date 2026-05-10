package com.example.weddingInvitation_b.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 커서 기반 페이징 공통 응답 DTO
 *
 * <p>ID 기반 커서를 사용한다. 첫 요청 시 cursor 파라미터를 생략하면 최신 데이터부터 반환하고,
 * 이후 요청에는 응답의 {@code nextCursor} 값을 cursor 파라미터로 전달하면 된다.</p>
 *
 * <pre>
 * 첫 요청:  GET /guestbook?size=10
 * 다음 요청: GET /guestbook?cursor=42&size=10
 * </pre>
 *
 * @param <T> 응답 항목 타입
 */
@Getter
@Builder
public class CursorPageResponseDto<T> {

    /** 현재 페이지 항목 목록 */
    private List<T> content;

    /**
     * 다음 페이지 요청 시 사용할 커서 (마지막 항목의 ID)
     *
     * <p>{@code null}이면 마지막 페이지임을 의미한다.</p>
     */
    private Long nextCursor;

    /** 다음 페이지 존재 여부 */
    private boolean hasNext;

    /** 현재 페이지의 실제 항목 수 */
    private int size;
}
