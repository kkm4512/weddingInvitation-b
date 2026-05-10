package com.example.weddingInvitation_b.controller;

import com.example.weddingInvitation_b.dto.response.ApiResponse;
import com.example.weddingInvitation_b.dto.response.SubwayItem;
import com.example.weddingInvitation_b.dto.response.TransportSuggestionsResponseDto.TransportItem;
import com.example.weddingInvitation_b.service.TransportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 교통수단 조회 API 컨트롤러
 *
 * <p>예식장 좌표 기반으로 주변 교통수단을 종류별 독립 API로 제공한다.</p>
 *
 * <ul>
 *   <li>지하철역: 카카오 카테고리 검색 API (SW8)</li>
 *   <li>버스정류장: 외부 API 미선정으로 현재 빈 리스트 반환</li>
 *   <li>주차장: 카카오 카테고리 검색 API (PK6)</li>
 * </ul>
 *
 * @see TransportService
 */
@RestController
@RequestMapping("/api/v1/transports")
@RequiredArgsConstructor
public class TransportController {

    private final TransportService transportService;

    /**
     * 주변 지하철역 출구 조회
     *
     * <p>예식장 좌표 기반으로 가장 가까운 지하철역 출구를 역별 1건씩 반환한다.</p>
     *
     * @param lat    위도
     * @param lng    경도
     * @param radius 탐색 반경 m (기본값: 1000)
     * @return 주변 지하철역 출구 목록 (도보시간 오름차순, 최대 5건)
     */
    @GetMapping("/subways")
    public ApiResponse<List<SubwayItem>> getSubways(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "1000") int radius) {
        return ApiResponse.success(transportService.getSubways(lat, lng, radius));
    }

    /**
     * 주변 버스정류장 조회
     *
     * <p>외부 API 미선정으로 현재 빈 리스트를 반환한다.</p>
     *
     * @param lat    위도
     * @param lng    경도
     * @param radius 탐색 반경 m (기본값: 500)
     * @return 주변 버스정류장 목록 (현재 항상 빈 리스트)
     */
    @GetMapping("/buses")
    public ApiResponse<List<TransportItem>> getBuses(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "500") int radius) {
        return ApiResponse.success(transportService.getBuses(lat, lng, radius));
    }

    /**
     * 주변 주차장 조회
     *
     * <p>예식장 좌표 기반으로 반경 내 주차장을 조회한다.</p>
     *
     * @param lat    위도
     * @param lng    경도
     * @param radius 탐색 반경 m (기본값: 500)
     * @return 주변 주차장 목록 (거리 오름차순, 최대 5건)
     */
    @GetMapping("/parkings")
    public ApiResponse<List<TransportItem>> getParkings(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "500") int radius) {
        return ApiResponse.success(transportService.getParkings(lat, lng, radius));
    }
}
