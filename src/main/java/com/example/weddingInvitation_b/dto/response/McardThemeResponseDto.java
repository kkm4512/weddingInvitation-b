package com.example.weddingInvitation_b.dto.response;

import com.example.weddingInvitation_b.domain.McardTheme;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 테마 설정 응답 DTO
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class McardThemeResponseDto {
    private Long themeId;
    private Long mcardId;
    private String themeStyle;
    private String color;
    private String fontFamily;
    private String fontWeight;
    private Boolean preventZoom;
    private Boolean enableScrollAnimation;

    public static McardThemeResponseDto from(McardTheme entity) {
        return McardThemeResponseDto.builder()
            .themeId(entity.getThemeId())
            .mcardId(entity.getMcard().getMcardId())
            .themeStyle(entity.getThemeStyle())
            .color(entity.getColor())
            .fontFamily(entity.getFontFamily())
            .fontWeight(entity.getFontWeight())
            .preventZoom(entity.getPreventZoom())
            .enableScrollAnimation(entity.getEnableScrollAnimation())
            .build();
    }
}
