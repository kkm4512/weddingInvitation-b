package com.example.weddingInvitation_b.controller;

import com.example.weddingInvitation_b.dto.response.AddressSearchResultDto;
import com.example.weddingInvitation_b.dto.response.ApiResponse;
import com.example.weddingInvitation_b.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 주소 검색 및 지도 이미지 API 컨트롤러
 *
 * <p>예식 장소 편집 화면에서 사용하는 두 가지 기능을 제공한다.</p>
 *
 * <ul>
 *   <li>주소 키워드 검색: 카카오 로컬 API — 입력 키워드에 매칭되는 주소 + 위경도 반환</li>
 *   <li>지도 이미지 반환: 네이버 Static Map API — 위경도 기반 PNG 이미지 직접 반환</li>
 * </ul>
 *
 * @see AddressService
 */
@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    /**
     * 주소 키워드 검색
     *
     * <p>카카오 로컬 API를 통해 키워드에 매칭되는 주소 목록을 반환한다.
     * 반환값에는 도로명 주소, 지번 주소, 위도, 경도, 페이지 메타(isEnd)가 포함된다.</p>
     *
     * @param query 검색할 주소 키워드 (예: "강남구 테헤란로", "역삼역")
     * @param page  페이지 번호 (기본값 1, 카카오 최대 45)
     * @param size  페이지당 결과 수 (기본값 10, 카카오 최대 15)
     * @return 주소 검색 결과 목록
     */
    @GetMapping("/search")
    public ApiResponse<AddressSearchResultDto> searchAddress(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(addressService.searchAddress(query, page, size));
    }

    /**
     * 위경도 좌표 기반 지도 이미지 반환
     *
     * <p>네이버 Static Map API를 통해 해당 좌표의 지도 이미지를 PNG로 반환한다.
     * 마커는 지정 좌표에 자동 표시되며, 이미지는 PNG 바이너리로 직접 반환된다.</p>
     *
     * @param lat    위도 (예: 37.5665)
     * @param lng    경도 (예: 126.9780)
     * @param width  이미지 너비 px (기본값: 400)
     * @param height 이미지 높이 px (기본값: 300)
     * @return PNG 지도 이미지 바이너리
     */
    @GetMapping("/map")
    public ResponseEntity<byte[]> getMapImage(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "400") int width,
            @RequestParam(defaultValue = "300") int height) {

        byte[] imageBytes = addressService.getMapImage(lat, lng, width, height);

        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_PNG)
            .body(imageBytes);
    }
}
