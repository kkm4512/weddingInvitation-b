package com.example.weddingInvitation_b.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 갤러리 레이아웃 설정 요청 DTO
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class GalleryLayoutRequestDto {
    /** 레이아웃 타입 (예: grid, slide) */
    private String layoutType;
}
