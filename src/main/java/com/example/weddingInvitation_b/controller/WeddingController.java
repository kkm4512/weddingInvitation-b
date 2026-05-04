package com.example.weddingInvitation_b.controller;

import com.example.weddingInvitation_b.dto.response.ApiResponse;
import com.example.weddingInvitation_b.dto.response.McardResponseDto;
import com.example.weddingInvitation_b.service.McardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 청첩장 공개 뷰 컨트롤러 (하객용)
 *
 * <p>초대 코드를 통해 인증 없이 청첩장을 조회하는 공개 API를 제공한다.
 * SecurityConfig에서 이 경로는 permitAll() 처리되어야 한다.</p>
 *
 * @see McardService
 */
@RestController
@RequestMapping("/api/v1/w")
@RequiredArgsConstructor
public class WeddingController {

    private final McardService mcardService;

    /**
     * 초대 코드로 공개 청첩장 조회 (하객 뷰)
     *
     * <p>인증 없이 접근 가능하다. inviteCode에 해당하는 청첩장 전체 데이터를 반환한다.</p>
     *
     * @param inviteCode 청첩장 초대 코드 (공개 URL)
     * @return 청첩장 공개 데이터
     */
    @GetMapping("/{inviteCode}")
    public ApiResponse<McardResponseDto> getPublicMcard(@PathVariable String inviteCode) {
        return ApiResponse.success(mcardService.getMcardByInviteCode(inviteCode));
    }
}
