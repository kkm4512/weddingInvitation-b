package com.example.weddingInvitation_b.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * 카카오 지도 URL 응답 DTO
 *
 * <p>위경도 좌표를 기반으로 생성한 카카오 지도 링크를 담아 반환한다.
 * 프론트엔드에서는 이 URL을 iframe 또는 링크로 렌더링하여 지도를 표시한다.</p>
 *
 * <p>카카오 스태틱 맵은 REST API로 제공되지 않으므로(JavaScript SDK 전용)
 * 서버에서 이미지를 직접 가져오는 대신 클라이언트가 렌더링할 수 있는 URL을 반환한다.</p>
 */
@Getter
@Builder
public class MapUrlResponseDto {

    /**
     * 카카오 지도 링크 URL
     *
     * <p>형식: {@code https://map.kakao.com/link/map/{장소명},{위도},{경도}}</p>
     * <p>예: {@code https://map.kakao.com/link/map/선택한 장소,37.4977,127.0279}</p>
     */
    private String mapUrl;

    /**
     * 위도
     */
    private double lat;

    /**
     * 경도
     */
    private double lng;
}
