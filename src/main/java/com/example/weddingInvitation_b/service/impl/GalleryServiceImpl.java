package com.example.weddingInvitation_b.service.impl;

import com.example.weddingInvitation_b.domain.GalleryPhoto;
import com.example.weddingInvitation_b.domain.Mcard;
import com.example.weddingInvitation_b.dto.request.GalleryLayoutRequestDto;
import com.example.weddingInvitation_b.dto.request.GalleryPhotoOrderRequestDto;
import com.example.weddingInvitation_b.dto.response.GalleryPhotoResponseDto;
import com.example.weddingInvitation_b.exception.EntityNotFoundException;
import com.example.weddingInvitation_b.repository.GalleryPhotoRepository;
import com.example.weddingInvitation_b.repository.McardRepository;
import com.example.weddingInvitation_b.service.FileService;
import com.example.weddingInvitation_b.service.GalleryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 갤러리 서비스 구현체
 *
 * <p>청첩장의 갤러리 사진을 조회, 추가, 순서 변경, 삭제한다.
 * 사진은 Cloudflare R2에 업로드되며 URL로 참조된다.</p>
 *
 * @see GalleryService
 * @see GalleryPhotoRepository
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GalleryServiceImpl implements GalleryService {

    private final GalleryPhotoRepository galleryPhotoRepository;
    private final McardRepository mcardRepository;
    private final FileService fileService;

    /**
     * 갤러리 사진 목록 조회 (순서 오름차순)
     *
     * @param mcardId 청첩장 ID
     * @return 갤러리 사진 목록
     */
    @Override
    public List<GalleryPhotoResponseDto> getGallery(Long mcardId) {
        return galleryPhotoRepository.findByMcardMcardIdOrderByDisplayOrderAsc(mcardId)
            .stream().map(GalleryPhotoResponseDto::from).collect(Collectors.toList());
    }

    /**
     * 갤러리 사진 파일 업로드 후 갤러리 등록
     *
     * <p>파일을 R2에 업로드하고 반환된 URL을 갤러리에 등록한다.
     * 순서는 현재 마지막 순서 다음으로 자동 부여된다.</p>
     *
     * @param mcardId 청첩장 ID
     * @param file    업로드할 이미지 파일
     * @return 저장된 사진 정보
     * @throws EntityNotFoundException 청첩장이 존재하지 않을 경우
     */
    @Override
    @Transactional
    public GalleryPhotoResponseDto addPhoto(Long mcardId, MultipartFile file) {
        Mcard mcard = mcardRepository.findById(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("청첩장을 찾을 수 없습니다. mcardId=" + mcardId));

        String imageUrl = fileService.upload(file, "gallery");

        int nextOrder = galleryPhotoRepository.findMaxDisplayOrderByMcardId(mcardId)
            .map(order -> order + 1).orElse(1);

        GalleryPhoto photo = GalleryPhoto.builder()
            .mcard(mcard)
            .imageUrl(imageUrl)
            .publicUrl(imageUrl)
            .displayOrder(nextOrder)
            .build();

        return GalleryPhotoResponseDto.from(galleryPhotoRepository.save(photo));
    }

    /**
     * 갤러리 사진 순서 변경
     *
     * <p>요청한 photoId 순서대로 displayOrder를 1부터 재부여한다.</p>
     *
     * @param mcardId    청첩장 ID
     * @param requestDto 새 순서의 photoId 목록
     * @return 순서 변경된 사진 목록
     */
    @Override
    @Transactional
    public List<GalleryPhotoResponseDto> updateOrder(Long mcardId, GalleryPhotoOrderRequestDto requestDto) {
        List<GalleryPhotoResponseDto> result = new ArrayList<>();
        List<Long> photoIds = requestDto.getPhotoIds();

        for (int i = 0; i < photoIds.size(); i++) {
            GalleryPhoto photo = galleryPhotoRepository.findById(photoIds.get(i))
                .orElseThrow(() -> new EntityNotFoundException("사진을 찾을 수 없습니다."));

            if (!photo.getMcard().getMcardId().equals(mcardId)) {
                throw new IllegalArgumentException("해당 사진은 요청한 청첩장에 속하지 않습니다.");
            }

            GalleryPhoto updated = GalleryPhoto.builder()
                .photoId(photo.getPhotoId()).mcard(photo.getMcard())
                .imageUrl(photo.getImageUrl()).displayOrder(i + 1)
                .layoutType(photo.getLayoutType()).build();

            result.add(GalleryPhotoResponseDto.from(galleryPhotoRepository.save(updated)));
        }
        return result;
    }

    /**
     * 갤러리 레이아웃 설정 저장
     *
     * <p>해당 청첩장의 모든 사진에 동일한 레이아웃을 적용한다.</p>
     *
     * @param mcardId    청첩장 ID
     * @param requestDto 레이아웃 타입
     * @return 첫 번째 사진 정보 (레이아웃 확인용)
     * @throws EntityNotFoundException 갤러리 사진이 없을 경우
     */
    @Override
    @Transactional
    public GalleryPhotoResponseDto updateLayout(Long mcardId, GalleryLayoutRequestDto requestDto) {
        List<GalleryPhoto> photos = galleryPhotoRepository.findByMcardMcardIdOrderByDisplayOrderAsc(mcardId);
        if (photos.isEmpty()) {
            throw new EntityNotFoundException("갤러리 사진이 없습니다. mcardId=" + mcardId);
        }

        GalleryPhoto first = null;
        for (GalleryPhoto photo : photos) {
            GalleryPhoto updated = GalleryPhoto.builder()
                .photoId(photo.getPhotoId()).mcard(photo.getMcard())
                .imageUrl(photo.getImageUrl()).displayOrder(photo.getDisplayOrder())
                .layoutType(requestDto.getLayoutType()).build();
            GalleryPhoto saved = galleryPhotoRepository.save(updated);
            if (first == null) first = saved;
        }

        return GalleryPhotoResponseDto.from(first);
    }

    /**
     * 갤러리 사진 삭제 (R2 파일도 함께 삭제)
     *
     * @param mcardId 청첩장 ID
     * @param photoId 삭제할 사진 ID
     * @throws EntityNotFoundException  사진이 없을 경우
     * @throws IllegalArgumentException 해당 청첩장 소속이 아닐 경우
     */
    @Override
    @Transactional
    public void deletePhoto(Long mcardId, Long photoId) {
        GalleryPhoto photo = galleryPhotoRepository.findById(photoId)
            .orElseThrow(() -> new EntityNotFoundException("사진을 찾을 수 없습니다. photoId=" + photoId));

        if (!photo.getMcard().getMcardId().equals(mcardId)) {
            throw new IllegalArgumentException("해당 사진은 요청한 청첩장에 속하지 않습니다.");
        }

        fileService.delete(photo.getImageUrl());
        galleryPhotoRepository.delete(photo);
    }
}
