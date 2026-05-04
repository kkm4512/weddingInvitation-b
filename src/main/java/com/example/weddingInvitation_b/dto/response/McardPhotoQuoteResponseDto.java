package com.example.weddingInvitation_b.dto.response;

import com.example.weddingInvitation_b.domain.McardPhotoQuote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 사진&글귀 응답 DTO
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class McardPhotoQuoteResponseDto {
    private Long photoQuoteId;
    private Long mcardId;
    private String imageUrl;
    private String quoteText;

    public static McardPhotoQuoteResponseDto from(McardPhotoQuote entity) {
        return McardPhotoQuoteResponseDto.builder()
            .photoQuoteId(entity.getPhotoQuoteId())
            .mcardId(entity.getMcard().getMcardId())
            .imageUrl(entity.getImageUrl())
            .quoteText(entity.getQuoteText())
            .build();
    }
}
