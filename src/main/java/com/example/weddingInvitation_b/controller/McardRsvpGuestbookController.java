package com.example.weddingInvitation_b.controller;

import com.example.weddingInvitation_b.dto.request.*;
import com.example.weddingInvitation_b.dto.response.*;
import com.example.weddingInvitation_b.service.GuestbookService;
import com.example.weddingInvitation_b.service.RsvpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /** RSVP 응답 목록 조회 (제작자용) */
    @GetMapping("/{mcardId}/rsvp")
    public ApiResponse<List<RsvpResponseResponseDto>> getRsvpResponses(@PathVariable Long mcardId) {
        return ApiResponse.success(rsvpService.getResponses(mcardId));
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

    /** 방명록 메시지 목록 조회 */
    @GetMapping("/{mcardId}/guestbook")
    public ApiResponse<List<GuestbookMessageResponseDto>> getGuestbookMessages(@PathVariable Long mcardId) {
        return ApiResponse.success(guestbookService.getMessages(mcardId));
    }

    /** 하객 방명록 메시지 작성 (인증 불필요) */
    @PostMapping("/{mcardId}/guestbook")
    public ApiResponse<GuestbookMessageResponseDto> addGuestbookMessage(
            @PathVariable Long mcardId,
            @RequestBody GuestbookMessageRequestDto requestDto) {
        return ApiResponse.success(guestbookService.addMessage(mcardId, requestDto));
    }

    /** 방명록 메시지 답글 작성 (제작자용) */
    @PutMapping("/{mcardId}/guestbook/{messageId}/reply")
    public ApiResponse<GuestbookMessageResponseDto> addReply(
            @PathVariable Long mcardId,
            @PathVariable Long messageId,
            @RequestBody GuestbookReplyRequestDto requestDto) {
        return ApiResponse.success(guestbookService.addReply(mcardId, messageId, requestDto));
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
