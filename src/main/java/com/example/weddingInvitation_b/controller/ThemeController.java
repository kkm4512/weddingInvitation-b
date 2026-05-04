package com.example.weddingInvitation_b.controller;

import com.example.weddingInvitation_b.dto.response.ApiResponse;
import com.example.weddingInvitation_b.dto.response.ThemeDto;
import com.example.weddingInvitation_b.exception.EntityNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 테마 목록 API 컨트롤러
 *
 * <p>사용 가능한 청첩장 테마 목록을 반환한다. 정적 데이터로 관리되며 인증이 불필요하다.</p>
 */
@RestController
@RequestMapping("/api/v1/themes")
public class ThemeController {

    private static final List<String> COMMON_FONTS = List.of("고운고딕", "나눔명조", "나눔고딕", "제주명조");
    private static final List<String> COMMON_COLORS = List.of("white", "beige", "pink", "lavender", "mint");

    /** 사용 가능한 테마 목록 (정적 데이터) */
    private static final List<ThemeDto> THEMES = List.of(
        ThemeDto.builder()
            .themeId("1").themeStyle("momentum").name("모멘트")
            .description("트렌디하고 감각적인 현대적 디자인")
            .supportedColors(COMMON_COLORS).supportedFonts(COMMON_FONTS).build(),
        ThemeDto.builder()
            .themeId("2").themeStyle("classic").name("클래식")
            .description("우아하고 격조 있는 클래식 디자인")
            .supportedColors(List.of("white", "ivory", "gold")).supportedFonts(List.of("나눔명조", "제주명조")).build(),
        ThemeDto.builder()
            .themeId("3").themeStyle("minimal").name("미니멀")
            .description("깔끔하고 세련된 미니멀 디자인")
            .supportedColors(List.of("white", "light-gray", "black")).supportedFonts(List.of("고운고딕", "나눔고딕")).build(),
        ThemeDto.builder()
            .themeId("4").themeStyle("romantic").name("로맨틱")
            .description("사랑스럽고 따뜻한 로맨틱 디자인")
            .supportedColors(List.of("pink", "rose", "peach", "lavender")).supportedFonts(COMMON_FONTS).build(),
        ThemeDto.builder()
            .themeId("5").themeStyle("nature").name("내추럴")
            .description("자연스럽고 편안한 내추럴 디자인")
            .supportedColors(List.of("beige", "green", "brown", "cream")).supportedFonts(COMMON_FONTS).build(),
        ThemeDto.builder()
            .themeId("6").themeStyle("luxury").name("럭셔리")
            .description("고급스럽고 품격 있는 럭셔리 디자인")
            .supportedColors(List.of("black", "gold", "champagne")).supportedFonts(List.of("나눔명조", "제주명조")).build()
    );

    private static final Map<String, ThemeDto> THEMES_BY_ID = THEMES.stream()
        .collect(Collectors.toMap(ThemeDto::getThemeId, Function.identity()));

    /**
     * 사용 가능한 테마 목록 조회
     *
     * @return 테마 목록
     */
    @GetMapping
    public ApiResponse<List<ThemeDto>> getThemes() {
        return ApiResponse.success(THEMES);
    }

    /**
     * 테마 상세 조회
     *
     * @param themeId 테마 ID
     * @return 테마 상세 정보 (색상/글꼴 옵션 포함)
     * @throws EntityNotFoundException 테마가 존재하지 않을 경우
     */
    @GetMapping("/{themeId}")
    public ApiResponse<ThemeDto> getTheme(@PathVariable String themeId) {
        ThemeDto theme = THEMES_BY_ID.get(themeId);
        if (theme == null) {
            throw new EntityNotFoundException("테마를 찾을 수 없습니다. themeId=" + themeId);
        }
        return ApiResponse.success(theme);
    }
}
