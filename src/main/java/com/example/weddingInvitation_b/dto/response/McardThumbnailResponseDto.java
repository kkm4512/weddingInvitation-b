package com.example.weddingInvitation_b.dto.response;

import com.example.weddingInvitation_b.domain.McardThumbnail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 공유 썸네일 응답 DTO
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class McardThumbnailResponseDto {
    private Long thumbnailId;
    private Long mcardId;
    private String kakaotalkThumbnailUrl;
    private String urlShareThumbnailUrl;

    public static McardThumbnailResponseDto from(McardThumbnail entity) {
        return McardThumbnailResponseDto.builder()
            .thumbnailId(entity.getThumbnailId())
            .mcardId(entity.getMcard().getMcardId())
            .kakaotalkThumbnailUrl(entity.getKakaotalkThumbnailUrl())
            .urlShareThumbnailUrl(entity.getUrlShareThumbnailUrl())
            .build();
    }
}
