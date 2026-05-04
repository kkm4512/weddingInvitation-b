package com.example.weddingInvitation_b.dto.response;

import com.example.weddingInvitation_b.domain.McardVenue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * 예식 장소 응답 DTO
 *
 * <p>transports는 서비스 레이어에서 별도 조회 후 주입된다.</p>
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class McardVenueResponseDto {
    private Long venueId;
    private Long mcardId;
    private String venueName;
    private String floorInfo;
    private String hallName;
    private String address;
    private Double latitude;
    private Double longitude;
    private Double lat;
    private Double lng;
    private String mapImageUrl;
    private Boolean showMap;
    private Boolean mapLocked;
    private Boolean showTransportIcons;
    private List<VenueTransportResponseDto> transports;

    public static McardVenueResponseDto from(McardVenue entity) {
        return McardVenueResponseDto.builder()
            .venueId(entity.getVenueId())
            .mcardId(entity.getMcard().getMcardId())
            .venueName(entity.getVenueName())
            .floorInfo(entity.getFloorInfo())
            .hallName(entity.getFloorInfo())
            .address(entity.getAddress())
            .latitude(entity.getLatitude())
            .longitude(entity.getLongitude())
            .lat(entity.getLatitude())
            .lng(entity.getLongitude())
            .mapImageUrl(entity.getMapImageUrl())
            .showMap(entity.getShowMap())
            .mapLocked(entity.getMapLocked())
            .showTransportIcons(entity.getShowTransportIcons())
            .transports(List.of())  // 서비스에서 별도 채움
            .build();
    }
}
