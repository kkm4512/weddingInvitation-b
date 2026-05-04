package com.example.weddingInvitation_b.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 글귀 저장 요청 DTO
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class McardQuoteRequestDto {
    private String quoteContent;
    private String fontSize;
}
