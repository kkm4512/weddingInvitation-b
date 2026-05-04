package com.example.weddingInvitation_b.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 메뉴 순서 저장 요청 DTO
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class McardSectionOrderRequestDto {
    /** 섹션 순서 배열 (예: ["couple","schedule","gallery"]) */
    private List<String> sectionOrder;
}
