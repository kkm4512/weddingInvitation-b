package com.example.weddingInvitation_b.dto.response;

import com.example.weddingInvitation_b.domain.GalleryPhoto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 갤러리 사진 응답 DTO
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class GalleryPhotoResponseDto {
    private Long photoId;
    private Long mcardId;
    private String imageUrl;
    private Integer displayOrder;
    private String layoutType;

    public static GalleryPhotoResponseDto from(GalleryPhoto entity) {
        return GalleryPhotoResponseDto.builder()
            .photoId(entity.getPhotoId())
            .mcardId(entity.getMcard().getMcardId())
            .imageUrl(entity.getImageUrl())
            .displayOrder(entity.getDisplayOrder())
            .layoutType(entity.getLayoutType())
            .build();
    }
}
