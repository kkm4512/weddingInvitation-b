package com.example.weddingInvitation_b.service;

import com.example.weddingInvitation_b.dto.request.McardVenueRequestDto;
import com.example.weddingInvitation_b.dto.request.VenueTransportRequestDto;
import com.example.weddingInvitation_b.dto.response.McardVenueResponseDto;
import com.example.weddingInvitation_b.dto.response.VenueTransportResponseDto;

/**
 * 예식 장소 서비스 인터페이스
 */
public interface McardVenueService {
    McardVenueResponseDto getVenue(Long mcardId);
    McardVenueResponseDto saveVenue(Long mcardId, McardVenueRequestDto requestDto);
    VenueTransportResponseDto addTransport(Long mcardId, VenueTransportRequestDto requestDto);
    VenueTransportResponseDto updateTransport(Long mcardId, Long transportId, VenueTransportRequestDto requestDto);
    void deleteTransport(Long mcardId, Long transportId);
}
