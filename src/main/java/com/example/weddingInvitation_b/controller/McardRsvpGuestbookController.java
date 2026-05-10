package com.example.weddingInvitation_b.controller;

import com.example.weddingInvitation_b.dto.request.*;
import com.example.weddingInvitation_b.dto.response.*;
import com.example.weddingInvitation_b.service.GuestbookService;
import com.example.weddingInvitation_b.service.RsvpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 참석의사(RSVP) + 방명록 API 컨트롤러
 *
 * <p>청첩장의 RSVP 설정/응답 및 방명록 설정/메시지를 관리한다.</p>
 *
 * @see RsvpService
 * @see GuestbookService
 */
@RestController
@RequestMapping("/api/v1/mcards")
@RequiredArgsConstructor
public class McardRsvpGuestbookController {

    private final RsvpService rsvpService;
    private final GuestbookService guestbookService;

    // ── RSVP ───────────────────────────────────────────────────

    /** RSVP 설정 조회 */
    @GetMapping("/{mcardId}/rsvp/settings")
    public ApiResponse<RsvpSettingResponseDto> getRsvpSetting(@PathVariable Long mcardId) {
        return ApiResponse.success(rsvpService.getSetting(mcardId));
    }

    /** RSVP 활성/비활성 설정 저장 */
    @PutMapping("/{mcardId}/rsvp/settings")
    public ApiResponse<RsvpSettingResponseDto> saveRsvpSetting(
            @PathVariable Long mcardId,
            @RequestBody RsvpSettingRequestDto requestDto) {
        return ApiResponse.success(rsvpService.saveSetting(mcardId, requestDto));
    }

    /** 하객 참석의사 응답 제출 (인증 불필요) */
    @PostMapping("/{mcardId}/rsvp")
    public ApiResponse<RsvpResponseResponseDto> submitRsvp(
            @PathVariable Long mcardId,
            @RequestBody RsvpResponseRequestDto requestDto) {
        return ApiResponse.success(rsvpService.submitResponse(mcardId, requestDto));
    }

    /**
     * RSVP 응답 목록 커서 페이징 조회 (제작자용)
     *
     * @param cursor 직전 페이지 마지막 responseId (첫 요청 시 생략)
     * @param size   페이지 크기 (기본값 10)
     */
    @GetMapping("/{mcardId}/rsvp")
    public ApiResponse<CursorPageResponseDto<RsvpResponseResponseDto>> getRsvpResponses(
            @PathVariable Long mcardId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(rsvpService.getResponses(mcardId, cursor, size));
    }

    // ── 방명록 ─────────────────────────────────────────────────

    /** 방명록 설정 조회 */
    @GetMapping("/{mcardId}/guestbook/settings")
    public ApiResponse<GuestbookSettingResponseDto> getGuestbookSetting(@PathVariable Long mcardId) {
        return ApiResponse.success(guestbookService.getSetting(mcardId));
    }

    /** 방명록 활성/비활성 설정 저장 */
    @PutMapping("/{mcardId}/guestbook/settings")
    public ApiResponse<GuestbookSettingResponseDto> saveGuestbookSetting(
            @PathVariable Long mcardId,
            @RequestBody GuestbookSettingRequestDto requestDto) {
        return ApiResponse.success(guestbookService.saveSetting(mcardId, requestDto));
    }

    /**
     * 방명록 메시지 커서 페이징 조회
     *
     * @param cursor 직전 페이지 마지막 messageId (첫 요청 시 생략)
     * @param size   페이지 크기 (기본값 10)
     */
    @GetMapping("/{mcardId}/guestbook")
    public ApiResponse<CursorPageResponseDto<GuestbookMessageResponseDto>> getGuestbookMessages(
            @PathVariable Long mcardId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(guestbookService.getMessages(mcardId, cursor, size));
    }

    /** 하객 방명록 메시지 작성 (인증 불필요) */
    @PostMapping("/{mcardId}/guestbook")
    public ApiResponse<GuestbookMessageResponseDto> addGuestbookMessage(
            @PathVariable Long mcardId,
            @RequestBody GuestbookMessageRequestDto requestDto) {
        return ApiResponse.success(guestbookService.addMessage(mcardId, requestDto));
    }

    /** 방명록 메시지 삭제 */
    @DeleteMapping("/{mcardId}/guestbook/{messageId}")
    public ApiResponse<Void> deleteGuestbookMessage(
            @PathVariable Long mcardId,
            @PathVariable Long messageId) {
        guestbookService.deleteMessage(mcardId, messageId);
        return ApiResponse.success();
    }
}
