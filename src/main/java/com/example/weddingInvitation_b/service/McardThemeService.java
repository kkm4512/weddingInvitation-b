package com.example.weddingInvitation_b.service;

import com.example.weddingInvitation_b.dto.request.McardThemeRequestDto;
import com.example.weddingInvitation_b.dto.response.McardThemeResponseDto;

/**
 * 테마 설정 서비스 인터페이스
 */
public interface McardThemeService {
    McardThemeResponseDto getTheme(Long mcardId);
    McardThemeResponseDto saveTheme(Long mcardId, McardThemeRequestDto requestDto);
}
