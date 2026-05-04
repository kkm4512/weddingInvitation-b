package com.example.weddingInvitation_b.service;

import com.example.weddingInvitation_b.dto.request.McardQuoteRequestDto;
import com.example.weddingInvitation_b.dto.response.McardQuoteResponseDto;

/**
 * 글귀 서비스 인터페이스
 */
public interface McardQuoteService {
    McardQuoteResponseDto getQuote(Long mcardId);
    McardQuoteResponseDto saveQuote(Long mcardId, McardQuoteRequestDto requestDto);
}
