package com.example.weddingInvitation_b.service;

import com.example.weddingInvitation_b.dto.response.UserResponseDto;

/**
 * 인증 서비스 인터페이스
 */
public interface AuthService {
    UserResponseDto getCurrentUser(Long userId);
    UserResponseDto loginWithProvider(String provider, String providerId, String name, String email);
    void logout(Long userId);
}

