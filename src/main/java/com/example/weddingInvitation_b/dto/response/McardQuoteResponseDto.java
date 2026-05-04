package com.example.weddingInvitation_b.dto.response;

import com.example.weddingInvitation_b.domain.McardQuote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 글귀 응답 DTO
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class McardQuoteResponseDto {
    private Long quoteId;
    private Long mcardId;
    private String quoteContent;
    private String fontSize;

    public static McardQuoteResponseDto from(McardQuote entity) {
        return McardQuoteResponseDto.builder()
            .quoteId(entity.getQuoteId())
            .mcardId(entity.getMcard().getMcardId())
            .quoteContent(entity.getQuoteContent())
            .fontSize(entity.getFontSize())
            .build();
    }
}
