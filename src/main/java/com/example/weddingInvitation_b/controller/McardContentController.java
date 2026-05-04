package com.example.weddingInvitation_b.controller;

import com.example.weddingInvitation_b.dto.request.*;
import com.example.weddingInvitation_b.dto.response.*;
import com.example.weddingInvitation_b.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 청첩장 콘텐츠 API 컨트롤러
 *
 * <p>청첩장의 각 섹션 콘텐츠(모시는 글, 동영상, BGM, 글귀, 사진&글귀,
 * 썸네일, 화환, 메뉴순서, 테마)를 조회하고 저장하는 API를 제공한다.
 * 모든 기능은 GET(조회) + PUT(저장/upsert) 패턴을 따른다.</p>
 *
 * @see McardGreetingService
 * @see McardVideoService
 * @see McardBgmService
 * @see McardQuoteService
 * @see McardPhotoQuoteService
 * @see McardThumbnailService
 * @see McardWreathService
 * @see McardSectionOrderService
 * @see McardThemeService
 */
@RestController
@RequestMapping("/api/v1/mcards")
@RequiredArgsConstructor
public class McardContentController {

    private final McardGreetingService mcardGreetingService;
    private final McardVideoService mcardVideoService;
    private final McardBgmService mcardBgmService;
    private final McardQuoteService mcardQuoteService;
    private final McardPhotoQuoteService mcardPhotoQuoteService;
    private final McardThumbnailService mcardThumbnailService;
    private final McardWreathService mcardWreathService;
    private final McardSectionOrderService mcardSectionOrderService;
    private final McardThemeService mcardThemeService;

    // ─────────────────────────────────────────────────────────────
    // 테마 설정
    // ─────────────────────────────────────────────────────────────

    /** 청첩장 테마 설정 조회 */
    @GetMapping("/{mcardId}/theme")
    public ApiResponse<McardThemeResponseDto> getTheme(@PathVariable Long mcardId) {
        return ApiResponse.success(mcardThemeService.getTheme(mcardId));
    }

    /** 청첩장 테마 설정 저장 */
    @PutMapping("/{mcardId}/theme")
    public ApiResponse<McardThemeResponseDto> saveTheme(
            @PathVariable Long mcardId,
            @RequestBody McardThemeRequestDto requestDto) {
        return ApiResponse.success(mcardThemeService.saveTheme(mcardId, requestDto));
    }

    // ─────────────────────────────────────────────────────────────
    // 모시는 글 (인사말)
    // ─────────────────────────────────────────────────────────────

    /** 인사말 조회 */
    @GetMapping("/{mcardId}/greeting")
    public ApiResponse<McardGreetingResponseDto> getGreeting(@PathVariable Long mcardId) {
        return ApiResponse.success(mcardGreetingService.getGreeting(mcardId));
    }

    /** 인사말 저장 */
    @PutMapping("/{mcardId}/greeting")
    public ApiResponse<McardGreetingResponseDto> saveGreeting(
            @PathVariable Long mcardId,
            @RequestBody McardGreetingRequestDto requestDto) {
        return ApiResponse.success(mcardGreetingService.saveGreeting(mcardId, requestDto));
    }

    // ─────────────────────────────────────────────────────────────
    // 동영상
    // ─────────────────────────────────────────────────────────────

    /** 동영상 정보 조회 */
    @GetMapping("/{mcardId}/video")
    public ApiResponse<McardVideoResponseDto> getVideo(@PathVariable Long mcardId) {
        return ApiResponse.success(mcardVideoService.getVideo(mcardId));
    }

    /** 동영상 URL 및 제목 저장 */
    @PutMapping("/{mcardId}/video")
    public ApiResponse<McardVideoResponseDto> saveVideo(
            @PathVariable Long mcardId,
            @RequestBody McardVideoRequestDto requestDto) {
        return ApiResponse.success(mcardVideoService.saveVideo(mcardId, requestDto));
    }

    // ─────────────────────────────────────────────────────────────
    // 배경음악 (BGM)
    // ─────────────────────────────────────────────────────────────

    /** 배경음악 설정 조회 */
    @GetMapping("/{mcardId}/bgm")
    public ApiResponse<McardBgmResponseDto> getBgm(@PathVariable Long mcardId) {
        return ApiResponse.success(mcardBgmService.getBgm(mcardId));
    }

    /** 배경음악 설정 저장 (URL 직접 입력) */
    @PutMapping("/{mcardId}/bgm")
    public ApiResponse<McardBgmResponseDto> saveBgm(
            @PathVariable Long mcardId,
            @RequestBody McardBgmRequestDto requestDto) {
        return ApiResponse.success(mcardBgmService.saveBgm(mcardId, requestDto));
    }

    /**
     * 배경음악 파일 업로드
     *
     * <p>음악 파일을 Cloudflare R2에 업로드하고 bgmUrl에 저장한다.</p>
     *
     * @param mcardId 청첩장 ID
     * @param file    업로드할 음악 파일 (multipart/form-data)
     * @return 저장된 BGM 정보
     */
    @PostMapping("/{mcardId}/bgm/upload")
    public ApiResponse<McardBgmResponseDto> uploadBgm(
            @PathVariable Long mcardId,
            @RequestParam("file") MultipartFile file) {
        return ApiResponse.success(mcardBgmService.uploadBgm(mcardId, file));
    }

    // ─────────────────────────────────────────────────────────────
    // 글귀
    // ─────────────────────────────────────────────────────────────

    /** 글귀 조회 */
    @GetMapping("/{mcardId}/quote")
    public ApiResponse<McardQuoteResponseDto> getQuote(@PathVariable Long mcardId) {
        return ApiResponse.success(mcardQuoteService.getQuote(mcardId));
    }

    /** 글귀 저장 */
    @PutMapping("/{mcardId}/quote")
    public ApiResponse<McardQuoteResponseDto> saveQuote(
            @PathVariable Long mcardId,
            @RequestBody McardQuoteRequestDto requestDto) {
        return ApiResponse.success(mcardQuoteService.saveQuote(mcardId, requestDto));
    }

    // ─────────────────────────────────────────────────────────────
    // 사진 & 글귀
    // ─────────────────────────────────────────────────────────────

    /** 사진&글귀 블록 조회 */
    @GetMapping("/{mcardId}/photo-quote")
    public ApiResponse<McardPhotoQuoteResponseDto> getPhotoQuote(@PathVariable Long mcardId) {
        return ApiResponse.success(mcardPhotoQuoteService.getPhotoQuote(mcardId));
    }

    /** 사진&글귀 블록 저장 (URL 직접 입력) */
    @PutMapping("/{mcardId}/photo-quote")
    public ApiResponse<McardPhotoQuoteResponseDto> savePhotoQuote(
            @PathVariable Long mcardId,
            @RequestBody McardPhotoQuoteRequestDto requestDto) {
        return ApiResponse.success(mcardPhotoQuoteService.savePhotoQuote(mcardId, requestDto));
    }

    /**
     * 사진&글귀 이미지 업로드
     *
     * <p>이미지 파일을 R2에 업로드하고 imageUrl에 저장한다.</p>
     *
     * @param mcardId 청첩장 ID
     * @param file    업로드할 이미지 파일 (multipart/form-data)
     * @return 저장된 사진&글귀 정보
     */
    @PostMapping("/{mcardId}/photo-quote/upload")
    public ApiResponse<McardPhotoQuoteResponseDto> uploadPhotoQuoteImage(
            @PathVariable Long mcardId,
            @RequestParam("file") MultipartFile file) {
        return ApiResponse.success(mcardPhotoQuoteService.uploadImage(mcardId, file));
    }

    // ─────────────────────────────────────────────────────────────
    // 공유 썸네일
    // ─────────────────────────────────────────────────────────────

    /** 공유 썸네일 설정 조회 */
    @GetMapping("/{mcardId}/thumbnail")
    public ApiResponse<McardThumbnailResponseDto> getThumbnail(@PathVariable Long mcardId) {
        return ApiResponse.success(mcardThumbnailService.getThumbnail(mcardId));
    }

    /** 공유 썸네일 설정 저장 (URL 직접 입력) */
    @PutMapping("/{mcardId}/thumbnail")
    public ApiResponse<McardThumbnailResponseDto> saveThumbnail(
            @PathVariable Long mcardId,
            @RequestBody McardThumbnailRequestDto requestDto) {
        return ApiResponse.success(mcardThumbnailService.saveThumbnail(mcardId, requestDto));
    }

    /**
     * 공유 썸네일 이미지 업로드
     *
     * <p>이미지 파일을 R2에 업로드하고 type에 따라 kakao 또는 url 썸네일 URL에 저장한다.</p>
     *
     * @param mcardId 청첩장 ID
     * @param file    업로드할 이미지 파일 (multipart/form-data)
     * @param type    썸네일 종류 - "kakao" 또는 "url" (기본값: "url")
     * @return 저장된 썸네일 정보
     */
    @PostMapping("/{mcardId}/thumbnail/upload")
    public ApiResponse<McardThumbnailResponseDto> uploadThumbnail(
            @PathVariable Long mcardId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "type", defaultValue = "url") String type) {
        return ApiResponse.success(mcardThumbnailService.uploadThumbnail(mcardId, file, type));
    }

    // ─────────────────────────────────────────────────────────────
    // 화환 보내기
    // ─────────────────────────────────────────────────────────────

    /** 화환 URL 설정 조회 */
    @GetMapping("/{mcardId}/wreath")
    public ApiResponse<McardWreathResponseDto> getWreath(@PathVariable Long mcardId) {
        return ApiResponse.success(mcardWreathService.getWreath(mcardId));
    }

    /** 화환 URL 설정 저장 */
    @PutMapping("/{mcardId}/wreath")
    public ApiResponse<McardWreathResponseDto> saveWreath(
            @PathVariable Long mcardId,
            @RequestBody McardWreathRequestDto requestDto) {
        return ApiResponse.success(mcardWreathService.saveWreath(mcardId, requestDto));
    }

    // ─────────────────────────────────────────────────────────────
    // 메뉴 순서
    // ─────────────────────────────────────────────────────────────

    /**
     * 섹션 노출 순서 조회
     *
     * @param mcardId 청첩장 ID
     * @return 섹션 순서 정보
     */
    @GetMapping("/{mcardId}/sections/order")
    public ApiResponse<McardSectionOrderResponseDto> getSectionOrder(@PathVariable Long mcardId) {
        return ApiResponse.success(mcardSectionOrderService.getSectionOrder(mcardId));
    }

    /**
     * 섹션 노출 순서 저장
     *
     * @param mcardId    청첩장 ID
     * @param requestDto 저장할 섹션 순서 정보
     * @return 저장된 섹션 순서 정보
     */
    @PutMapping("/{mcardId}/sections/order")
    public ApiResponse<McardSectionOrderResponseDto> saveSectionOrder(
            @PathVariable Long mcardId,
            @RequestBody McardSectionOrderRequestDto requestDto) {
        return ApiResponse.success(mcardSectionOrderService.saveSectionOrder(mcardId, requestDto));
    }
}
