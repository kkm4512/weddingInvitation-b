package com.example.weddingInvitation_b.service;

import com.example.weddingInvitation_b.dto.request.McardGreetingRequestDto;
import com.example.weddingInvitation_b.dto.response.McardGreetingResponseDto;

/**
 * 모시는 글(인사말) 서비스 인터페이스
 */
public interface McardGreetingService {
    McardGreetingResponseDto getGreeting(Long mcardId);
    McardGreetingResponseDto saveGreeting(Long mcardId, McardGreetingRequestDto requestDto);
}
