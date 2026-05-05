package com.example.weddingInvitation_b.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * 갤러리 사진 순서 변경 요청 DTO
 *
 * <p>각 사진의 ID와 명시적인 표시 순서(displayOrder)를 함께 전달한다.
 * displayOrder는 1부터 시작하는 양의 정수여야 하며 중복 없이 지정해야 한다.</p>
 *
 * <p>요청 예시:</p>
 * <pre>
 * {
 *   "photos": [
 *     { "photoId": 3, "displayOrder": 1 },
 *     { "photoId": 1, "displayOrder": 2 },
 *     { "photoId": 5, "displayOrder": 3 }
 *   ]
 * }
 * </pre>
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class GalleryPhotoOrderRequestDto {

    /** 사진 ID + 표시 순서 쌍 목록 */
    private List<PhotoOrderItem> photos;

    /**
     * 개별 사진의 순서 지정 항목
     */
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class PhotoOrderItem {
        /** 사진 ID */
        private Long photoId;
        /** 표시 순서 (1-based) */
        private Integer displayOrder;
    }
}
