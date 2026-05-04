package com.example.weddingInvitation_b.service;

import com.example.weddingInvitation_b.dto.request.McardCreateRequestDto;
import com.example.weddingInvitation_b.dto.response.McardResponseDto;

import java.util.List;

/**
 * 청첩장 서비스 인터페이스
 */
public interface McardService {
    List<McardResponseDto> getMyMcards(Long userId);
    McardResponseDto getMcard(Long mcardId);
    McardResponseDto getMcardByInviteCode(String inviteCode);
    McardResponseDto createMcard(McardCreateRequestDto requestDto, Long userId);
    McardResponseDto updateMcard(Long mcardId, McardCreateRequestDto requestDto, Long userId);
    void deleteMcard(Long mcardId, Long userId);
}
