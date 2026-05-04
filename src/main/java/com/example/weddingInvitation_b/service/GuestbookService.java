package com.example.weddingInvitation_b.service;
import com.example.weddingInvitation_b.dto.request.GuestbookMessageRequestDto;
import com.example.weddingInvitation_b.dto.request.GuestbookReplyRequestDto;
import com.example.weddingInvitation_b.dto.request.GuestbookSettingRequestDto;
import com.example.weddingInvitation_b.dto.response.GuestbookMessageResponseDto;
import com.example.weddingInvitation_b.dto.response.GuestbookSettingResponseDto;
import java.util.List;
/** 방명록 서비스 인터페이스 */
public interface GuestbookService {
    GuestbookSettingResponseDto getSetting(Long mcardId);
    GuestbookSettingResponseDto saveSetting(Long mcardId, GuestbookSettingRequestDto requestDto);
    List<GuestbookMessageResponseDto> getMessages(Long mcardId);
    GuestbookMessageResponseDto addMessage(Long mcardId, GuestbookMessageRequestDto requestDto);
    GuestbookMessageResponseDto addReply(Long mcardId, Long messageId, GuestbookReplyRequestDto requestDto);
    void deleteMessage(Long mcardId, Long messageId);
}
