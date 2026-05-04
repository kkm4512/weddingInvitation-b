package com.example.weddingInvitation_b.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 교통수단 안내 저장 요청 DTO
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class VenueTransportRequestDto {
    @JsonAlias({"type"})
    private String transportType;
    private String description;
    private Integer displayOrder;
}
