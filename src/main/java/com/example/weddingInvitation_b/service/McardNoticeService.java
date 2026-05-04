package com.example.weddingInvitation_b.service;
import com.example.weddingInvitation_b.dto.request.McardNoticeRequestDto;
import com.example.weddingInvitation_b.dto.response.McardNoticeResponseDto;
import java.util.List;
/** 안내사항 서비스 인터페이스 */
public interface McardNoticeService {
    List<McardNoticeResponseDto> getNotices(Long mcardId);
    McardNoticeResponseDto addNotice(Long mcardId, McardNoticeRequestDto requestDto);
    McardNoticeResponseDto updateNotice(Long mcardId, Long noticeId, McardNoticeRequestDto requestDto);
    void deleteNotice(Long mcardId, Long noticeId);
}
