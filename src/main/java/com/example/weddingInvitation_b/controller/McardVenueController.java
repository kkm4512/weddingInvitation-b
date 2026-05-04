package com.example.weddingInvitation_b.controller;

import com.example.weddingInvitation_b.dto.request.McardVenueRequestDto;
import com.example.weddingInvitation_b.dto.request.VenueTransportRequestDto;
import com.example.weddingInvitation_b.dto.response.ApiResponse;
import com.example.weddingInvitation_b.dto.response.McardVenueResponseDto;
import com.example.weddingInvitation_b.dto.response.VenueTransportResponseDto;
import com.example.weddingInvitation_b.service.McardVenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 예식 장소 API 컨트롤러
 *
 * <p>청첩장의 예식 장소 정보와 교통수단 안내를 조회하고 관리한다.</p>
 *
 * @see McardVenueService
 */
@RestController
@RequestMapping("/api/v1/mcards")
@RequiredArgsConstructor
public class McardVenueController {

    private final McardVenueService mcardVenueService;

    /**
     * 예식 장소 조회
     *
     * @param mcardId 청첩장 ID
     * @return 예식 장소 정보 (교통수단 포함)
     */
    @GetMapping("/{mcardId}/venue")
    public ApiResponse<McardVenueResponseDto> getVenue(@PathVariable Long mcardId) {
        return ApiResponse.success(mcardVenueService.getVenue(mcardId));
    }

    /**
     * 예식 장소 저장
     *
     * @param mcardId    청첩장 ID
     * @param requestDto 저장할 예식 장소 정보
     * @return 저장된 예식 장소 정보
     */
    @PutMapping("/{mcardId}/venue")
    public ApiResponse<McardVenueResponseDto> saveVenue(
            @PathVariable Long mcardId,
            @RequestBody McardVenueRequestDto requestDto) {
        return ApiResponse.success(mcardVenueService.saveVenue(mcardId, requestDto));
    }

    /**
     * 교통수단 안내 추가
     *
     * @param mcardId    청첩장 ID
     * @param requestDto 추가할 교통수단 정보
     * @return 추가된 교통수단 정보
     */
    @PostMapping("/{mcardId}/venue/transports")
    public ApiResponse<VenueTransportResponseDto> addTransport(
            @PathVariable Long mcardId,
            @RequestBody VenueTransportRequestDto requestDto) {
        return ApiResponse.success(mcardVenueService.addTransport(mcardId, requestDto));
    }

    /**
     * 교통수단 안내 수정
     *
     * @param mcardId     청첩장 ID
     * @param transportId 수정할 교통수단 ID
     * @param requestDto  수정할 정보
     * @return 수정된 교통수단 정보
     */
    @PutMapping("/{mcardId}/venue/transports/{transportId}")
    public ApiResponse<VenueTransportResponseDto> updateTransport(
            @PathVariable Long mcardId,
            @PathVariable Long transportId,
            @RequestBody VenueTransportRequestDto requestDto) {
        return ApiResponse.success(mcardVenueService.updateTransport(mcardId, transportId, requestDto));
    }

    /**
     * 교통수단 안내 삭제
     *
     * @param mcardId     청첩장 ID
     * @param transportId 삭제할 교통수단 ID
     * @return 빈 성공 응답
     */
    @DeleteMapping("/{mcardId}/venue/transports/{transportId}")
    public ApiResponse<Void> deleteTransport(
            @PathVariable Long mcardId,
            @PathVariable Long transportId) {
        mcardVenueService.deleteTransport(mcardId, transportId);
        return ApiResponse.success();
    }
}
