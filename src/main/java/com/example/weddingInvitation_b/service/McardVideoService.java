package com.example.weddingInvitation_b.service;

import com.example.weddingInvitation_b.dto.request.McardVideoRequestDto;
import com.example.weddingInvitation_b.dto.response.McardVideoResponseDto;

/**
 * 동영상 서비스 인터페이스
 */
public interface McardVideoService {
    McardVideoResponseDto getVideo(Long mcardId);
    McardVideoResponseDto saveVideo(Long mcardId, McardVideoRequestDto requestDto);
}
