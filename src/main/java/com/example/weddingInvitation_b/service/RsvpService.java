package com.example.weddingInvitation_b.service;
import com.example.weddingInvitation_b.dto.request.RsvpResponseRequestDto;
import com.example.weddingInvitation_b.dto.request.RsvpSettingRequestDto;
import com.example.weddingInvitation_b.dto.response.RsvpResponseResponseDto;
import com.example.weddingInvitation_b.dto.response.RsvpSettingResponseDto;
import java.util.List;
/** RSVP 서비스 인터페이스 */
public interface RsvpService {
    RsvpSettingResponseDto getSetting(Long mcardId);
    RsvpSettingResponseDto saveSetting(Long mcardId, RsvpSettingRequestDto requestDto);
    RsvpResponseResponseDto submitResponse(Long mcardId, RsvpResponseRequestDto requestDto);
    List<RsvpResponseResponseDto> getResponses(Long mcardId);
}
