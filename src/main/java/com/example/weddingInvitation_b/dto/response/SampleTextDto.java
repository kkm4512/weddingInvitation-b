package com.example.weddingInvitation_b.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 샘플 문구 DTO
 *
 * <p>인사말, 글귀, 안내사항, BGM 샘플 목록 응답에 사용된다.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SampleTextDto {

    /** 샘플 고유 ID */
    private String sampleId;

    /** 샘플 제목 (카테고리 표시용, nullable) */
    private String title;

    /** 샘플 본문 내용 */
    private String content;
}
