package com.example.weddingInvitation_b.service;

import com.example.weddingInvitation_b.dto.request.GalleryLayoutRequestDto;
import com.example.weddingInvitation_b.dto.request.GalleryPhotoOrderRequestDto;
import com.example.weddingInvitation_b.dto.response.GalleryPhotoResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 갤러리 서비스 인터페이스
 */
public interface GalleryService {
    List<GalleryPhotoResponseDto> getGallery(Long mcardId);

    /**
     * 사진 파일을 R2에 업로드하고 갤러리에 등록한다.
     *
     * @param mcardId 청첩장 ID
     * @param file    업로드할 이미지 파일
     * @return 추가된 사진 정보
     */
    GalleryPhotoResponseDto addPhoto(Long mcardId, MultipartFile file);

    List<GalleryPhotoResponseDto> updateOrder(Long mcardId, GalleryPhotoOrderRequestDto requestDto);
    GalleryPhotoResponseDto updateLayout(Long mcardId, GalleryLayoutRequestDto requestDto);
    void deletePhoto(Long mcardId, Long photoId);
}
