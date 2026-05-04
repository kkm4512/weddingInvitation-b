package com.example.weddingInvitation_b.service;
import com.example.weddingInvitation_b.dto.request.McardContactRequestDto;
import com.example.weddingInvitation_b.dto.response.McardContactResponseDto;
import java.util.List;
/** 연락처 서비스 인터페이스 */
public interface McardContactService {
    List<McardContactResponseDto> getContacts(Long mcardId);
    List<McardContactResponseDto> saveContacts(Long mcardId, McardContactRequestDto requestDto);
}
