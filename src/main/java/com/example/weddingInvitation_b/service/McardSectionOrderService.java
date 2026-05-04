package com.example.weddingInvitation_b.service;

import com.example.weddingInvitation_b.dto.request.McardSectionOrderRequestDto;
import com.example.weddingInvitation_b.dto.response.McardSectionOrderResponseDto;

/**
 * 메뉴 순서 서비스 인터페이스
 */
public interface McardSectionOrderService {
    McardSectionOrderResponseDto getSectionOrder(Long mcardId);
    McardSectionOrderResponseDto saveSectionOrder(Long mcardId, McardSectionOrderRequestDto requestDto);
}
