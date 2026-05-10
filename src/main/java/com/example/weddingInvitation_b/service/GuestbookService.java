package com.example.weddingInvitation_b.service;

import com.example.weddingInvitation_b.dto.request.GuestbookMessageRequestDto;
import com.example.weddingInvitation_b.dto.request.GuestbookSettingRequestDto;
import com.example.weddingInvitation_b.dto.response.CursorPageResponseDto;
import com.example.weddingInvitation_b.dto.response.GuestbookMessageResponseDto;
import com.example.weddingInvitation_b.dto.response.GuestbookSettingResponseDto;

/** 방명록 서비스 인터페이스 */
public interface GuestbookService {

    GuestbookSettingResponseDto getSetting(Long mcardId);

    GuestbookSettingResponseDto saveSetting(Long mcardId, GuestbookSettingRequestDto requestDto);

    /**
     * 방명록 메시지 커서 페이징 조회
     *
     * @param mcardId 청첩장 ID
     * @param cursor  직전 페이지 마지막 항목의 messageId (첫 요청 시 null)
     * @param size    페이지 크기
     */
    CursorPageResponseDto<GuestbookMessageResponseDto> getMessages(Long mcardId, Long cursor, int size);

    GuestbookMessageResponseDto addMessage(Long mcardId, GuestbookMessageRequestDto requestDto);

    void deleteMessage(Long mcardId, Long messageId);
}
