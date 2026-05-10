package com.example.weddingInvitation_b.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 예식 장소 저장 요청 DTO
 *
 * <p>mapImageUrl은 클라이언트가 전송하지 않는다.
 * PUT 저장 시 서버가 lat/lng로 네이버 Static Map 이미지를 자동 생성하여 R2에 저장한다.</p>
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class McardVenueRequestDto {
    private String venueName;
    @JsonAlias({"hallName"})
    private String floorInfo;
    private String address;
    private Double lat;
    private Double lng;
    private Boolean showMap;
    @JsonAlias({"lockMap"})
    private Boolean mapLocked;
    private Boolean showTransportIcons;
}
