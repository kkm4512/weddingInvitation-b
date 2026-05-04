package com.example.weddingInvitation_b.service.impl;

import com.example.weddingInvitation_b.domain.Mcard;
import com.example.weddingInvitation_b.domain.McardVenue;
import com.example.weddingInvitation_b.domain.VenueTransport;
import com.example.weddingInvitation_b.dto.request.McardVenueRequestDto;
import com.example.weddingInvitation_b.dto.request.VenueTransportRequestDto;
import com.example.weddingInvitation_b.dto.response.McardVenueResponseDto;
import com.example.weddingInvitation_b.dto.response.VenueTransportResponseDto;
import com.example.weddingInvitation_b.exception.EntityNotFoundException;
import com.example.weddingInvitation_b.repository.McardRepository;
import com.example.weddingInvitation_b.repository.McardVenueRepository;
import com.example.weddingInvitation_b.repository.VenueTransportRepository;
import com.example.weddingInvitation_b.service.McardVenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 예식 장소 서비스 구현체
 *
 * <p>청첩장의 예식 장소 정보와 교통수단 안내를 조회하고 관리한다.
 * McardVenue 엔티티에 transports 컬렉션이 없으므로 서비스에서 별도 조회 후 DTO에 주입한다.</p>
 *
 * @see McardVenueService
 * @see McardVenueRepository
 * @see VenueTransportRepository
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class McardVenueServiceImpl implements McardVenueService {

    private final McardVenueRepository mcardVenueRepository;
    private final VenueTransportRepository venueTransportRepository;
    private final McardRepository mcardRepository;

    /**
     * 예식 장소 조회 (교통수단 목록 포함)
     *
     * @param mcardId 청첩장 ID
     * @return 예식 장소 정보 (교통수단 목록 포함)
     * @throws EntityNotFoundException 장소 정보가 없을 경우
     */
    @Override
    public McardVenueResponseDto getVenue(Long mcardId) {
        McardVenue venue = mcardVenueRepository.findByMcardMcardId(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("예식 장소 정보를 찾을 수 없습니다. mcardId=" + mcardId));

        // 교통수단 목록 별도 조회 후 DTO에 주입
        List<VenueTransportResponseDto> transports = venueTransportRepository
            .findByVenueVenueIdOrderByDisplayOrderAsc(venue.getVenueId())
            .stream().map(VenueTransportResponseDto::from).collect(Collectors.toList());

        McardVenueResponseDto dto = McardVenueResponseDto.from(venue);
        dto.setTransports(transports);
        return dto;
    }

    /**
     * 예식 장소 저장 (upsert)
     *
     * @param mcardId    청첩장 ID
     * @param requestDto 저장할 예식 장소 정보
     * @return 저장된 예식 장소 정보 (교통수단 포함)
     * @throws EntityNotFoundException 청첩장이 존재하지 않을 경우
     */
    @Override
    @Transactional
    public McardVenueResponseDto saveVenue(Long mcardId, McardVenueRequestDto requestDto) {
        Mcard mcard = mcardRepository.findById(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("청첩장을 찾을 수 없습니다. mcardId=" + mcardId));

        McardVenue existing = mcardVenueRepository.findByMcardMcardId(mcardId)
            .orElse(McardVenue.builder().mcard(mcard).build());

        McardVenue venue = McardVenue.builder()
            .venueId(existing.getVenueId())
            .mcard(mcard)
            .venueName(requestDto.getVenueName() != null ? requestDto.getVenueName() : existing.getVenueName())
            .floorInfo(requestDto.getFloorInfo() != null ? requestDto.getFloorInfo() : existing.getFloorInfo())
            .address(requestDto.getAddress() != null ? requestDto.getAddress() : existing.getAddress())
            .latitude(requestDto.getLatitude() != null ? requestDto.getLatitude() : existing.getLatitude())
            .longitude(requestDto.getLongitude() != null ? requestDto.getLongitude() : existing.getLongitude())
            .mapImageUrl(requestDto.getMapImageUrl() != null ? requestDto.getMapImageUrl() : existing.getMapImageUrl())
            .showMap(requestDto.getShowMap() != null ? requestDto.getShowMap() : existing.getShowMap())
            .mapLocked(requestDto.getMapLocked() != null ? requestDto.getMapLocked() : existing.getMapLocked())
            .showTransportIcons(requestDto.getShowTransportIcons() != null ? requestDto.getShowTransportIcons() : existing.getShowTransportIcons())
            .build();

        McardVenue saved = mcardVenueRepository.save(venue);

        List<VenueTransportResponseDto> transports = venueTransportRepository
            .findByVenueVenueIdOrderByDisplayOrderAsc(saved.getVenueId())
            .stream().map(VenueTransportResponseDto::from).collect(Collectors.toList());

        McardVenueResponseDto dto = McardVenueResponseDto.from(saved);
        dto.setTransports(transports);
        return dto;
    }

    /**
     * 교통수단 안내 추가
     *
     * @param mcardId    청첩장 ID
     * @param requestDto 추가할 교통수단 정보
     * @return 추가된 교통수단 정보
     * @throws EntityNotFoundException 예식 장소 정보가 없을 경우
     */
    @Override
    @Transactional
    public VenueTransportResponseDto addTransport(Long mcardId, VenueTransportRequestDto requestDto) {
        McardVenue venue = mcardVenueRepository.findByMcardMcardId(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("예식 장소 정보를 찾을 수 없습니다. mcardId=" + mcardId));

        int nextOrder = requestDto.getDisplayOrder() != null
            ? requestDto.getDisplayOrder()
            : venueTransportRepository.findByVenueVenueIdOrderByDisplayOrderAsc(venue.getVenueId()).size() + 1;

        VenueTransport transport = VenueTransport.builder()
            .venue(venue).transportType(requestDto.getTransportType())
            .description(requestDto.getDescription()).displayOrder(nextOrder).build();

        return VenueTransportResponseDto.from(venueTransportRepository.save(transport));
    }

    /**
     * 교통수단 안내 수정
     *
     * @param mcardId     청첩장 ID
     * @param transportId 수정할 교통수단 ID
     * @param requestDto  수정할 정보
     * @return 수정된 교통수단 정보
     * @throws EntityNotFoundException  교통수단 정보가 없을 경우
     * @throws IllegalArgumentException 해당 청첩장 소속이 아닐 경우
     */
    @Override
    @Transactional
    public VenueTransportResponseDto updateTransport(Long mcardId, Long transportId, VenueTransportRequestDto requestDto) {
        VenueTransport transport = venueTransportRepository.findById(transportId)
            .orElseThrow(() -> new EntityNotFoundException("교통수단 정보를 찾을 수 없습니다. transportId=" + transportId));

        if (!transport.getVenue().getMcard().getMcardId().equals(mcardId))
            throw new IllegalArgumentException("해당 교통수단은 요청한 청첩장에 속하지 않습니다. mcardId=" + mcardId);

        VenueTransport updated = VenueTransport.builder()
            .transportId(transport.getTransportId()).venue(transport.getVenue())
            .transportType(requestDto.getTransportType() != null ? requestDto.getTransportType() : transport.getTransportType())
            .description(requestDto.getDescription() != null ? requestDto.getDescription() : transport.getDescription())
            .displayOrder(requestDto.getDisplayOrder() != null ? requestDto.getDisplayOrder() : transport.getDisplayOrder())
            .build();

        return VenueTransportResponseDto.from(venueTransportRepository.save(updated));
    }

    /**
     * 교통수단 안내 삭제
     *
     * @param mcardId     청첩장 ID
     * @param transportId 삭제할 교통수단 ID
     * @throws EntityNotFoundException  교통수단 정보가 없을 경우
     * @throws IllegalArgumentException 해당 청첩장 소속이 아닐 경우
     */
    @Override
    @Transactional
    public void deleteTransport(Long mcardId, Long transportId) {
        VenueTransport transport = venueTransportRepository.findById(transportId)
            .orElseThrow(() -> new EntityNotFoundException("교통수단 정보를 찾을 수 없습니다. transportId=" + transportId));

        if (!transport.getVenue().getMcard().getMcardId().equals(mcardId))
            throw new IllegalArgumentException("해당 교통수단은 요청한 청첩장에 속하지 않습니다. mcardId=" + mcardId);

        venueTransportRepository.delete(transport);
    }
}
