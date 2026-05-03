package com.example.weddingInvitation_b.controller;

import com.example.weddingInvitation_b.dto.request.McardCreateRequestDto;
import com.example.weddingInvitation_b.dto.response.ApiResponse;
import com.example.weddingInvitation_b.dto.response.McardResponseDto;
import com.example.weddingInvitation_b.service.McardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 청첩장 관련 API 컨트롤러
 * 
 * <p>청첩장의 CRUD 요청을 수신하고 McardService에 비즈니스 로직을 위임한다.</p>
 */
@RestController
@RequestMapping("/api/v1/mcards")
@RequiredArgsConstructor
public class McardController {

    private final McardService mcardService;

    /**
     * 내 청첩장 목록 조회
     * 
     * <p>현재 로그인한 사용자의 모든 청첩장을 조회한다.</p>
     * 
     * @return 사용자의 청첩장 목록
     */
    @GetMapping
    public ApiResponse<List<McardResponseDto>> getMyMcards() {
        // 샘플 구현: 실제로는 JWT 토큰에서 사용자 ID를 추출
        Long sampleUserId = 1L;
        List<McardResponseDto> mcards = mcardService.getMyMcards(sampleUserId);
        return ApiResponse.success(mcards);
    }

    /**
     * 청첩장 단건 조회 (편집용)
     * 
     * <p>특정 청첩장의 상세 정보를 조회한다.</p>
     * 
     * @param mcardId 조회할 청첩장 ID
     * @return 청첩장 상세 정보
     */
    @GetMapping("/{mcardId}")
    public ApiResponse<McardResponseDto> getMcard(@PathVariable Long mcardId) {
        McardResponseDto mcard = mcardService.getMcard(mcardId);
        return ApiResponse.success(mcard);
    }

    /**
     * 청첩장 신규 생성
     * 
     * <p>새로운 청첩장을 생성합니다.</p>
     * 
     * @param requestDto 청첩장 생성 정보
     * @return 생성된 청첩장 정보
     */
    @PostMapping
    public ApiResponse<McardResponseDto> createMcard(@RequestBody McardCreateRequestDto requestDto) {
        // 샘플 구현: 실제로는 JWT 토큰에서 사용자 ID를 추출
        Long sampleUserId = 1L;
        McardResponseDto createdMcard = mcardService.createMcard(requestDto, sampleUserId);
        return ApiResponse.success(createdMcard);
    }

    /**
     * 청첩장 정보 업데이트
     * 
     * @param mcardId 업데이트할 청첩장 ID
     * @param requestDto 업데이트 정보
     * @return 업데이트된 청첩장 정보
     */
    @PutMapping("/{mcardId}")
    public ApiResponse<McardResponseDto> updateMcard(
            @PathVariable Long mcardId,
            @RequestBody McardCreateRequestDto requestDto) {
        McardResponseDto updatedMcard = mcardService.updateMcard(mcardId, requestDto);
        return ApiResponse.success(updatedMcard);
    }

    /**
     * 청첩장 삭제
     * 
     * @param mcardId 삭제할 청첩장 ID
     * @return 삭제 완료 메시지
     */
    @DeleteMapping("/{mcardId}")
    public ApiResponse<Void> deleteMcard(@PathVariable Long mcardId) {
        mcardService.deleteMcard(mcardId);
        return ApiResponse.success();
    }

    /**
     * 청첩장 미리보기 (공개 뷰)
     * 
     * <p>초대 코드로 하객이 볼 수 있는 청첩장을 조회한다.</p>
     * 
     * @param inviteCode 초대 코드
     * @return 청첩장 미리보기 정보
     */
    @GetMapping("/preview/{inviteCode}")
    public ApiResponse<McardResponseDto> getPreview(@PathVariable String inviteCode) {
        McardResponseDto mcard = mcardService.getMcardByInviteCode(inviteCode);
        return ApiResponse.success(mcard);
    }
}
