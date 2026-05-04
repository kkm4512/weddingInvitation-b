package com.example.weddingInvitation_b.service;

import com.example.weddingInvitation_b.dto.request.McardWreathRequestDto;
import com.example.weddingInvitation_b.dto.response.McardWreathResponseDto;

/**
 * 화환 보내기 서비스 인터페이스
 */
public interface McardWreathService {
    McardWreathResponseDto getWreath(Long mcardId);
    McardWreathResponseDto saveWreath(Long mcardId, McardWreathRequestDto requestDto);
}
