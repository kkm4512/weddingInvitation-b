package com.example.weddingInvitation_b.service;

import com.example.weddingInvitation_b.dto.request.McardCoupleRequestDto;
import com.example.weddingInvitation_b.dto.response.McardCoupleResponseDto;

/**
 * 청첩장 신랑·신부 정보 서비스 인터페이스
 */
public interface McardCoupleService {
    McardCoupleResponseDto getCouple(Long mcardId);
    McardCoupleResponseDto saveCouple(Long mcardId, McardCoupleRequestDto requestDto);
}
