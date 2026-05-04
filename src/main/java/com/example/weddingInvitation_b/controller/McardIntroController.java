package com.example.weddingInvitation_b.controller;

import com.example.weddingInvitation_b.dto.request.McardIntroRequestDto;
import com.example.weddingInvitation_b.dto.response.ApiResponse;
import com.example.weddingInvitation_b.dto.response.IntroStyleDto;
import com.example.weddingInvitation_b.dto.response.McardIntroResponseDto;
import com.example.weddingInvitation_b.service.McardIntroService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 인트로 설정 API 컨트롤러
 *
 * <p>인트로 레이아웃 스타일 목록 조회 및 청첩장별 인트로 스타일 저장 API를 제공한다.</p>
 *
 * @see McardIntroService
 */
@RestController
@RequiredArgsConstructor
public class McardIntroController {

    private final McardIntroService mcardIntroService;

    /** 사용 가능한 인트로 스타일 목록 (정적 데이터) */
    private static final List<IntroStyleDto> INTRO_STYLES = List.of(
        IntroStyleDto.builder().styleKey("DEFAULT").name("기본").description("기본 세로 레이아웃").build(),
        IntroStyleDto.builder().styleKey("ARGE").name("아지").description("큰 이미지 중심 레이아웃").build(),
        IntroStyleDto.builder().styleKey("OVAL").name("타원").description("타원형 프레임 레이아웃").build(),
        IntroStyleDto.builder().styleKey("EDGE").name("엣지").description("모서리 강조 레이아웃").build(),
        IntroStyleDto.builder().styleKey("FILL").name("채우기").description("전체 화면 채우기 레이아웃").build(),
        IntroStyleDto.builder().styleKey("CLASSIC").name("클래식").description("클래식 세로 레이아웃").build(),
        IntroStyleDto.builder().styleKey("MINIMAL").name("미니멀").description("텍스트 중심 미니멀 레이아웃").build()
    );

    /**
     * 인트로 레이아웃 스타일 목록 조회
     *
     * <p>선택 가능한 모든 인트로 스타일을 반환한다. 인증 불필요.</p>
     *
     * @return 인트로 스타일 목록
     */
    @GetMapping("/api/v1/intros")
    public ApiResponse<List<IntroStyleDto>> getIntroStyles() {
        return ApiResponse.success(INTRO_STYLES);
    }

    /**
     * 청첩장 인트로 설정 조회
     *
     * @param mcardId 청첩장 ID
     * @return 인트로 설정 정보
     */
    @GetMapping("/api/v1/mcards/{mcardId}/intro")
    public ApiResponse<McardIntroResponseDto> getIntro(@PathVariable Long mcardId) {
        return ApiResponse.success(mcardIntroService.getIntro(mcardId));
    }

    /**
     * 청첩장 인트로 스타일 저장 (upsert)
     *
     * @param mcardId    청첩장 ID
     * @param requestDto 저장할 인트로 스타일 정보
     * @return 저장된 인트로 설정 정보
     */
    @PutMapping("/api/v1/mcards/{mcardId}/intro")
    public ApiResponse<McardIntroResponseDto> saveIntro(
            @PathVariable Long mcardId,
            @RequestBody McardIntroRequestDto requestDto) {
        return ApiResponse.success(mcardIntroService.saveIntro(mcardId, requestDto));
    }
}
