package com.example.weddingInvitation_b.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * 갤러리 사진 순서 변경 요청 DTO
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class GalleryPhotoOrderRequestDto {
    /** 순서를 변경할 사진 ID 목록 (순서대로) */
    private List<Long> photoIds;
}
