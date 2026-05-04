package com.example.weddingInvitation_b.dto.response;

import com.example.weddingInvitation_b.domain.VenueTransport;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 교통수단 안내 응답 DTO
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class VenueTransportResponseDto {
    private Long transportId;
    private Long venueId;
    @JsonProperty("type")
    private String transportType;
    private String description;
    private Integer displayOrder;

    public static VenueTransportResponseDto from(VenueTransport entity) {
        return VenueTransportResponseDto.builder()
            .transportId(entity.getTransportId())
            .venueId(entity.getVenue().getVenueId())
            .transportType(entity.getTransportType())
            .description(entity.getDescription())
            .displayOrder(entity.getDisplayOrder())
            .build();
    }
}
