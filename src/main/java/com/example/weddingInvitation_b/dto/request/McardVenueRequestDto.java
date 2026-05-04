package com.example.weddingInvitation_b.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 예식 장소 저장 요청 DTO
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class McardVenueRequestDto {
    private String venueName;
    private String floorInfo;
    private String address;
    private Double latitude;
    private Double longitude;
    private String mapImageUrl;
    private Boolean showMap;
    private Boolean mapLocked;
    private Boolean showTransportIcons;
}
