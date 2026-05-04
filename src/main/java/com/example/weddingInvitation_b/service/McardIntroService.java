package com.example.weddingInvitation_b.service;

import com.example.weddingInvitation_b.dto.request.McardIntroRequestDto;
import com.example.weddingInvitation_b.dto.response.McardIntroResponseDto;

/**
 * 청첩장 인트로 설정 서비스 인터페이스
 */
public interface McardIntroService {

    /**
     * 청첩장 인트로 설정 조회
     *
     * @param mcardId 청첩장 ID
     * @return 인트로 설정 정보
     */
    McardIntroResponseDto getIntro(Long mcardId);

    /**
     * 청첩장 인트로 스타일 저장 (upsert)
     *
     * @param mcardId    청첩장 ID
     * @param requestDto 저장할 인트로 스타일 정보
     * @return 저장된 인트로 설정 정보
     */
    McardIntroResponseDto saveIntro(Long mcardId, McardIntroRequestDto requestDto);
}
