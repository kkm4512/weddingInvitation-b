package com.example.weddingInvitation_b.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 테마 정보 DTO
 *
 * <p>GET /themes, GET /themes/{themeId} 응답에 사용된다.
 * themeStyle은 McardTheme.themeStyle에 저장되는 값과 일치한다.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThemeDto {

    /** 테마 고유 ID */
    private String themeId;

    /** 테마 스타일 키 (McardTheme.themeStyle 저장 값) */
    private String themeStyle;

    /** 테마 표시 이름 */
    private String name;

    /** 테마 설명 */
    private String description;

    /** 지원 색상 목록 (예: ["white", "beige", "pink"]) */
    private List<String> supportedColors;

    /** 지원 글꼴 목록 (예: ["고운고딕", "나눔명조"]) */
    private List<String> supportedFonts;
}
