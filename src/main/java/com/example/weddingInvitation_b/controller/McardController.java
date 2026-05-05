package com.example.weddingInvitation_b.controller;

import com.example.weddingInvitation_b.dto.request.McardCreateRequestDto;
import com.example.weddingInvitation_b.dto.request.McardCoupleRequestDto;
import com.example.weddingInvitation_b.dto.request.McardScheduleRequestDto;
import com.example.weddingInvitation_b.dto.request.McardSectionOrderRequestDto;
import com.example.weddingInvitation_b.dto.response.ApiResponse;
import com.example.weddingInvitation_b.dto.response.McardCoupleResponseDto;
import com.example.weddingInvitation_b.dto.response.McardResponseDto;
import com.example.weddingInvitation_b.dto.response.McardScheduleResponseDto;
import com.example.weddingInvitation_b.dto.response.McardSectionOrderResponseDto;
import com.example.weddingInvitation_b.service.McardCoupleService;
import com.example.weddingInvitation_b.service.McardScheduleService;
import com.example.weddingInvitation_b.service.McardSectionOrderService;
import com.example.weddingInvitation_b.service.McardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 청첩장 관련 API 컨트롤러
 *
 * <p>청첩장 CRUD 및 신랑·신부 정보, 예식 일시, 미리보기 요청을 수신하고
 * 각 서비스에 비즈니스 로직을 위임한다.</p>
 *
 * @see McardService
 * @see McardCoupleService
 * @see McardScheduleService
 */
@RestController
@RequestMapping("/api/v1/mcards")
@RequiredArgsConstructor
public class McardController {

    private final McardService mcardService;
    private final McardCoupleService mcardCoupleService;
    private final McardScheduleService mcardScheduleService;
    private final McardSectionOrderService mcardSectionOrderService;

    // ─────────────────────────────────────────────────────────────
    // 청첩장 CRUD
    // ─────────────────────────────────────────────────────────────

    /**
     * 내 청첩장 목록 조회
     *
     * @return 현재 로그인 사용자의 청첩장 목록
     */
    @GetMapping
    public ApiResponse<List<McardResponseDto>> getMyMcards() {
        Long userId = getCurrentUserId();
        return ApiResponse.success(mcardService.getMyMcards(userId));
    }

    /**
     * 청첩장 단건 조회 (편집용)
     *
     * @param mcardId 조회할 청첩장 ID
     * @return 청첩장 상세 정보
     */
    @GetMapping("/{mcardId}")
    public ApiResponse<McardResponseDto> getMcard(@PathVariable Long mcardId) {
        return ApiResponse.success(mcardService.getMcard(mcardId));
    }

    /**
     * 청첩장 미리보기 데이터 조회
     *
     * @param mcardId 미리보기할 청첩장 ID
     * @return 청첩장 미리보기 데이터
     */
    @GetMapping("/{mcardId}/preview")
    public ApiResponse<McardResponseDto> getPreview(@PathVariable Long mcardId) {
        return ApiResponse.success(mcardService.getMcard(mcardId));
    }

    /**
     * 청첩장 신규 생성
     *
     * @param requestDto 청첩장 생성 정보
     * @return 생성된 청첩장 정보
     */
    @PostMapping
    public ApiResponse<McardResponseDto> createMcard(@RequestBody McardCreateRequestDto requestDto) {
        Long userId = getCurrentUserId();
        return ApiResponse.success(mcardService.createMcard(requestDto, userId));
    }

    /**
     * 청첩장 전체 저장 (편집 내용 저장)
     *
     * @param mcardId    수정할 청첩장 ID
     * @param requestDto 수정 정보
     * @return 수정된 청첩장 정보
     */
    @PutMapping("/{mcardId}")
    public ApiResponse<McardResponseDto> updateMcard(
            @PathVariable Long mcardId,
            @RequestBody McardCreateRequestDto requestDto) {
        Long userId = getCurrentUserId();
        return ApiResponse.success(mcardService.updateMcard(mcardId, requestDto, userId));
    }

    /**
     * 청첩장 삭제 (soft delete)
     *
     * @param mcardId 삭제할 청첩장 ID
     * @return 빈 성공 응답
     */
    @DeleteMapping("/{mcardId}")
    public ApiResponse<Void> deleteMcard(@PathVariable Long mcardId) {
        Long userId = getCurrentUserId();
        mcardService.deleteMcard(mcardId, userId);
        return ApiResponse.success();
    }

    // ─────────────────────────────────────────────────────────────
    // 신랑·신부 정보
    // ─────────────────────────────────────────────────────────────

    /**
     * 신랑·신부 정보 조회
     *
     * @param mcardId 청첩장 ID
     * @return 신랑·신부 정보
     */
    @GetMapping("/{mcardId}/couple")
    public ApiResponse<McardCoupleResponseDto> getCouple(@PathVariable Long mcardId) {
        return ApiResponse.success(mcardCoupleService.getCouple(mcardId));
    }

    /**
     * 신랑·신부 정보 저장
     *
     * @param mcardId    청첩장 ID
     * @param requestDto 저장할 신랑·신부 정보
     * @return 저장된 신랑·신부 정보
     */
    @PutMapping("/{mcardId}/couple")
    public ApiResponse<McardCoupleResponseDto> saveCouple(
            @PathVariable Long mcardId,
            @RequestBody McardCoupleRequestDto requestDto) {
        return ApiResponse.success(mcardCoupleService.saveCouple(mcardId, requestDto));
    }

    // ─────────────────────────────────────────────────────────────
    // 예식 일시
    // ─────────────────────────────────────────────────────────────

    /**
     * 예식 일시 조회
     *
     * @param mcardId 청첩장 ID
     * @return 예식 일시 정보
     */
    @GetMapping("/{mcardId}/schedule")
    public ApiResponse<McardScheduleResponseDto> getSchedule(@PathVariable Long mcardId) {
        return ApiResponse.success(mcardScheduleService.getSchedule(mcardId));
    }

    /**
     * 예식 일시 저장
     *
     * @param mcardId    청첩장 ID
     * @param requestDto 저장할 예식 일시 정보
     * @return 저장된 예식 일시 정보
     */
    @PutMapping("/{mcardId}/schedule")
    public ApiResponse<McardScheduleResponseDto> saveSchedule(
            @PathVariable Long mcardId,
            @RequestBody McardScheduleRequestDto requestDto) {
        return ApiResponse.success(mcardScheduleService.saveSchedule(mcardId, requestDto));
    }

    // ─────────────────────────────────────────────────────────────
    // 섹션 순서
    // ─────────────────────────────────────────────────────────────

    /**
     * 섹션 순서 조회
     *
     * @param mcardId 청첩장 ID
     * @return 섹션 순서 정보
     */
    @GetMapping("/{mcardId}/section-order")
    public ApiResponse<McardSectionOrderResponseDto> getSectionOrder(@PathVariable Long mcardId) {
        return ApiResponse.success(mcardSectionOrderService.getSectionOrder(mcardId));
    }

    /**
     * 섹션 순서 저장
     *
     * @param mcardId    청첩장 ID
     * @param requestDto 저장할 섹션 순서 정보
     * @return 저장된 섹션 순서 정보
     */
    @PutMapping("/{mcardId}/section-order")
    public ApiResponse<McardSectionOrderResponseDto> saveSectionOrder(
            @PathVariable Long mcardId,
            @RequestBody McardSectionOrderRequestDto requestDto) {
        return ApiResponse.success(mcardSectionOrderService.saveSectionOrder(mcardId, requestDto));
    }

    // ─────────────────────────────────────────────────────────────
    // 공통 유틸
    // ─────────────────────────────────────────────────────────────

    /**
     * SecurityContext에서 현재 로그인 사용자 ID를 추출한다.
     *
     * @return 현재 로그인 사용자 ID
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Long.parseLong(authentication.getName());
    }
}
