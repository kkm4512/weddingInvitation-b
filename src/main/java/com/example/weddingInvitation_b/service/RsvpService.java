package com.example.weddingInvitation_b.service;

import com.example.weddingInvitation_b.dto.request.RsvpResponseRequestDto;
import com.example.weddingInvitation_b.dto.request.RsvpSettingRequestDto;
import com.example.weddingInvitation_b.dto.response.CursorPageResponseDto;
import com.example.weddingInvitation_b.dto.response.RsvpResponseResponseDto;
import com.example.weddingInvitation_b.dto.response.RsvpSettingResponseDto;

/** RSVP 서비스 인터페이스 */
public interface RsvpService {

    RsvpSettingResponseDto getSetting(Long mcardId);

    RsvpSettingResponseDto saveSetting(Long mcardId, RsvpSettingRequestDto requestDto);

    RsvpResponseResponseDto submitResponse(Long mcardId, RsvpResponseRequestDto requestDto);

    /**
     * RSVP 응답 목록 커서 페이징 조회 (제작자용)
     *
     * @param mcardId 청첩장 ID
     * @param cursor  직전 페이지 마지막 항목의 responseId (첫 요청 시 null)
     * @param size    페이지 크기
     */
    CursorPageResponseDto<RsvpResponseResponseDto> getResponses(Long mcardId, Long cursor, int size);
}
