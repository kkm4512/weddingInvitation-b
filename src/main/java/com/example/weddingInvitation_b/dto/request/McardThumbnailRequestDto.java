package com.example.weddingInvitation_b.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 공유 썸네일 저장 요청 DTO
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class McardThumbnailRequestDto {
    private String kakaotalkThumbnailUrl;
    private String urlShareThumbnailUrl;
}
