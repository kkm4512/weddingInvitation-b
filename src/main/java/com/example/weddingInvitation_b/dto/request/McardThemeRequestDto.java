package com.example.weddingInvitation_b.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 테마 설정 저장 요청 DTO
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class McardThemeRequestDto {
    private String themeStyle;
    private String color;
    private String fontFamily;
    private String fontWeight;
    private Boolean preventZoom;
    private Boolean enableScrollAnimation;
}
