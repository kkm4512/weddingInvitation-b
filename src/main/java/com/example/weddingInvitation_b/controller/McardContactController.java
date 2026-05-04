package com.example.weddingInvitation_b.controller;

import com.example.weddingInvitation_b.dto.request.BankAccountRequestDto;
import com.example.weddingInvitation_b.dto.request.McardContactRequestDto;
import com.example.weddingInvitation_b.dto.request.McardNoticeRequestDto;
import com.example.weddingInvitation_b.dto.response.*;
import com.example.weddingInvitation_b.service.BankAccountService;
import com.example.weddingInvitation_b.service.McardContactService;
import com.example.weddingInvitation_b.service.McardNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 연락하기·계좌번호·안내사항 API 컨트롤러
 *
 * <p>청첩장의 연락처 정보, 계좌번호, 안내사항을 관리한다.</p>
 *
 * @see McardContactService
 * @see BankAccountService
 * @see McardNoticeService
 */
@RestController
@RequestMapping("/api/v1/mcards")
@RequiredArgsConstructor
public class McardContactController {

    private final McardContactService mcardContactService;
    private final BankAccountService bankAccountService;
    private final McardNoticeService mcardNoticeService;

    // ── 연락하기 ───────────────────────────────────────────────

    /**
     * 연락처 정보 조회
     * @param mcardId 청첩장 ID
     */
    @GetMapping("/{mcardId}/contacts")
    public ApiResponse<List<McardContactResponseDto>> getContacts(@PathVariable Long mcardId) {
        return ApiResponse.success(mcardContactService.getContacts(mcardId));
    }

    /**
     * 연락처 정보 저장 (전체 교체)
     * @param mcardId    청첩장 ID
     * @param requestDto 저장할 연락처 목록
     */
    @PutMapping("/{mcardId}/contacts")
    public ApiResponse<List<McardContactResponseDto>> saveContacts(
            @PathVariable Long mcardId,
            @RequestBody McardContactRequestDto requestDto) {
        return ApiResponse.success(mcardContactService.saveContacts(mcardId, requestDto));
    }

    // ── 계좌번호 ───────────────────────────────────────────────

    /**
     * 계좌 목록 조회
     * @param mcardId 청첩장 ID
     */
    @GetMapping("/{mcardId}/accounts")
    public ApiResponse<List<BankAccountResponseDto>> getAccounts(@PathVariable Long mcardId) {
        return ApiResponse.success(bankAccountService.getAccounts(mcardId));
    }

    /**
     * 계좌 추가
     * @param mcardId    청첩장 ID
     * @param requestDto 추가할 계좌 정보
     */
    @PostMapping("/{mcardId}/accounts")
    public ApiResponse<BankAccountResponseDto> addAccount(
            @PathVariable Long mcardId,
            @RequestBody BankAccountRequestDto requestDto) {
        return ApiResponse.success(bankAccountService.addAccount(mcardId, requestDto));
    }

    /**
     * 계좌 수정
     * @param mcardId   청첩장 ID
     * @param accountId 수정할 계좌 ID
     * @param requestDto 수정할 계좌 정보
     */
    @PutMapping("/{mcardId}/accounts/{accountId}")
    public ApiResponse<BankAccountResponseDto> updateAccount(
            @PathVariable Long mcardId,
            @PathVariable Long accountId,
            @RequestBody BankAccountRequestDto requestDto) {
        return ApiResponse.success(bankAccountService.updateAccount(mcardId, accountId, requestDto));
    }

    /**
     * 계좌 삭제
     * @param mcardId   청첩장 ID
     * @param accountId 삭제할 계좌 ID
     */
    @DeleteMapping("/{mcardId}/accounts/{accountId}")
    public ApiResponse<Void> deleteAccount(
            @PathVariable Long mcardId,
            @PathVariable Long accountId) {
        bankAccountService.deleteAccount(mcardId, accountId);
        return ApiResponse.success();
    }

    // ── 안내사항 ───────────────────────────────────────────────

    /**
     * 안내사항 목록 조회
     * @param mcardId 청첩장 ID
     */
    @GetMapping("/{mcardId}/notices")
    public ApiResponse<List<McardNoticeResponseDto>> getNotices(@PathVariable Long mcardId) {
        return ApiResponse.success(mcardNoticeService.getNotices(mcardId));
    }

    /**
     * 안내사항 추가
     * @param mcardId    청첩장 ID
     * @param requestDto 추가할 안내사항 정보
     */
    @PostMapping("/{mcardId}/notices")
    public ApiResponse<McardNoticeResponseDto> addNotice(
            @PathVariable Long mcardId,
            @RequestBody McardNoticeRequestDto requestDto) {
        return ApiResponse.success(mcardNoticeService.addNotice(mcardId, requestDto));
    }

    /**
     * 안내사항 수정
     * @param mcardId    청첩장 ID
     * @param noticeId   수정할 안내사항 ID
     * @param requestDto 수정할 정보
     */
    @PutMapping("/{mcardId}/notices/{noticeId}")
    public ApiResponse<McardNoticeResponseDto> updateNotice(
            @PathVariable Long mcardId,
            @PathVariable Long noticeId,
            @RequestBody McardNoticeRequestDto requestDto) {
        return ApiResponse.success(mcardNoticeService.updateNotice(mcardId, noticeId, requestDto));
    }

    /**
     * 안내사항 삭제
     * @param mcardId  청첩장 ID
     * @param noticeId 삭제할 안내사항 ID
     */
    @DeleteMapping("/{mcardId}/notices/{noticeId}")
    public ApiResponse<Void> deleteNotice(
            @PathVariable Long mcardId,
            @PathVariable Long noticeId) {
        mcardNoticeService.deleteNotice(mcardId, noticeId);
        return ApiResponse.success();
    }
}
