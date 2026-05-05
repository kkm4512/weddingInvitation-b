package com.example.weddingInvitation_b.controller;

import com.example.weddingInvitation_b.dto.request.GalleryLayoutRequestDto;
import com.example.weddingInvitation_b.dto.request.GalleryPhotoOrderRequestDto;
import com.example.weddingInvitation_b.dto.response.ApiResponse;
import com.example.weddingInvitation_b.dto.response.GalleryPhotoResponseDto;
import com.example.weddingInvitation_b.service.GalleryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 갤러리 API 컨트롤러
 *
 * <p>청첩장 갤러리의 사진 업로드, 목록 조회, 순서 변경, 레이아웃 설정, 삭제 기능을 제공한다.</p>
 *
 * @see GalleryService
 */
@RestController
@RequestMapping("/api/v1/mcards")
@RequiredArgsConstructor
public class McardGalleryController {

    private final GalleryService galleryService;

    /**
     * 갤러리 사진 목록 조회
     *
     * @param mcardId 청첩장 ID
     * @return 갤러리 사진 목록 (순서 오름차순)
     */
    @GetMapping("/{mcardId}/gallery")
    public ApiResponse<List<GalleryPhotoResponseDto>> getGallery(@PathVariable Long mcardId) {
        return ApiResponse.success(galleryService.getGallery(mcardId));
    }

    /**
     * 갤러리 사진 업로드
     *
     * <p>이미지 파일을 Cloudflare R2에 업로드하고 갤러리에 등록한다.</p>
     *
     * @param mcardId 청첩장 ID
     * @param file    업로드할 이미지 파일 (multipart/form-data)
     * @return 추가된 사진 정보
     */
    @PostMapping("/{mcardId}/gallery")
    public ApiResponse<GalleryPhotoResponseDto> addPhoto(
            @PathVariable Long mcardId,
            @RequestParam("file") MultipartFile file) {
        return ApiResponse.success(galleryService.addPhoto(mcardId, file));
    }

    /**
     * 갤러리 사진 순서 변경
     *
     * <p>각 사진의 photoId와 displayOrder를 명시적으로 지정하여 순서를 변경한다.
     * displayOrder는 중복 없이 지정해야 한다.</p>
     *
     * @param mcardId    청첩장 ID
     * @param requestDto photoId + displayOrder 쌍 목록
     * @return 순서 변경된 사진 목록 (displayOrder 오름차순)
     */
    @PutMapping("/{mcardId}/gallery/order")
    public ApiResponse<List<GalleryPhotoResponseDto>> updateOrder(
            @PathVariable Long mcardId,
            @RequestBody GalleryPhotoOrderRequestDto requestDto) {
        return ApiResponse.success(galleryService.updateOrder(mcardId, requestDto));
    }

    /**
     * 갤러리 레이아웃 설정 저장
     *
     * @param mcardId    청첩장 ID
     * @param requestDto 레이아웃 타입
     * @return 업데이트된 사진 정보
     */
    @PutMapping("/{mcardId}/gallery/layout")
    public ApiResponse<GalleryPhotoResponseDto> updateLayout(
            @PathVariable Long mcardId,
            @RequestBody GalleryLayoutRequestDto requestDto) {
        return ApiResponse.success(galleryService.updateLayout(mcardId, requestDto));
    }

    /**
     * 갤러리 사진 삭제 (R2 파일도 함께 삭제)
     *
     * @param mcardId 청첩장 ID
     * @param photoId 삭제할 사진 ID
     * @return 빈 성공 응답
     */
    @DeleteMapping("/{mcardId}/gallery/{photoId}")
    public ApiResponse<Void> deletePhoto(
            @PathVariable Long mcardId,
            @PathVariable Long photoId) {
        galleryService.deletePhoto(mcardId, photoId);
        return ApiResponse.success();
    }
}
