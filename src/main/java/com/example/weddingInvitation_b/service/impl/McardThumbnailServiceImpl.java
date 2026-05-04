package com.example.weddingInvitation_b.service.impl;

import com.example.weddingInvitation_b.domain.Mcard;
import com.example.weddingInvitation_b.domain.McardThumbnail;
import com.example.weddingInvitation_b.dto.request.McardThumbnailRequestDto;
import com.example.weddingInvitation_b.dto.response.McardThumbnailResponseDto;
import com.example.weddingInvitation_b.exception.EntityNotFoundException;
import com.example.weddingInvitation_b.repository.McardRepository;
import com.example.weddingInvitation_b.repository.McardThumbnailRepository;
import com.example.weddingInvitation_b.service.FileService;
import com.example.weddingInvitation_b.service.McardThumbnailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 공유 썸네일 서비스 구현체
 *
 * @see McardThumbnailService
 * @see McardThumbnailRepository
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class McardThumbnailServiceImpl implements McardThumbnailService {

    private final McardThumbnailRepository mcardThumbnailRepository;
    private final McardRepository mcardRepository;
    private final FileService fileService;

    @Override
    public McardThumbnailResponseDto getThumbnail(Long mcardId) {
        McardThumbnail thumbnail = mcardThumbnailRepository.findByMcardMcardId(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("썸네일 정보를 찾을 수 없습니다. mcardId=" + mcardId));
        return McardThumbnailResponseDto.from(thumbnail);
    }

    @Override
    @Transactional
    public McardThumbnailResponseDto saveThumbnail(Long mcardId, McardThumbnailRequestDto requestDto) {
        Mcard mcard = mcardRepository.findById(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("청첩장을 찾을 수 없습니다. mcardId=" + mcardId));

        McardThumbnail existing = mcardThumbnailRepository.findByMcardMcardId(mcardId)
            .orElse(McardThumbnail.builder().mcard(mcard).build());

        McardThumbnail thumbnail = McardThumbnail.builder()
            .thumbnailId(existing.getThumbnailId()).mcard(mcard)
            .kakaotalkThumbnailUrl(requestDto.getKakaotalkThumbnailUrl() != null
                ? requestDto.getKakaotalkThumbnailUrl() : existing.getKakaotalkThumbnailUrl())
            .urlShareThumbnailUrl(requestDto.getUrlShareThumbnailUrl() != null
                ? requestDto.getUrlShareThumbnailUrl() : existing.getUrlShareThumbnailUrl())
            .build();

        return McardThumbnailResponseDto.from(mcardThumbnailRepository.save(thumbnail));
    }

    /**
     * 썸네일 이미지를 R2에 업로드하고 해당 종류의 URL을 저장한다 (upsert).
     *
     * <p>type에 따라 kakaotalkThumbnailUrl 또는 urlShareThumbnailUrl에 저장한다.
     * 기존 이미지가 있으면 R2에서 삭제 후 교체한다.</p>
     *
     * @param mcardId 청첩장 ID
     * @param file    업로드할 썸네일 이미지
     * @param type    썸네일 종류 ("kakao" | "url")
     * @return 업로드 후 저장된 썸네일 정보
     * @throws EntityNotFoundException 청첩장이 존재하지 않을 경우
     */
    @Override
    @Transactional
    public McardThumbnailResponseDto uploadThumbnail(Long mcardId, MultipartFile file, String type) {
        Mcard mcard = mcardRepository.findById(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("청첩장을 찾을 수 없습니다. mcardId=" + mcardId));

        McardThumbnail existing = mcardThumbnailRepository.findByMcardMcardId(mcardId)
            .orElse(McardThumbnail.builder().mcard(mcard).build());

        String newUrl = fileService.upload(file, "thumbnail");

        String kakaoUrl = existing.getKakaotalkThumbnailUrl();
        String shareUrl = existing.getUrlShareThumbnailUrl();
        String kakaoPublicUrl = existing.getKakaotalkPublicUrl();
        String sharePublicUrl = existing.getUrlSharePublicUrl();

        if ("kakao".equalsIgnoreCase(type)) {
            if (kakaoUrl != null && !kakaoUrl.isBlank()) fileService.delete(kakaoUrl);
            kakaoUrl = newUrl;
            kakaoPublicUrl = newUrl;
        } else {
            if (shareUrl != null && !shareUrl.isBlank()) fileService.delete(shareUrl);
            shareUrl = newUrl;
            sharePublicUrl = newUrl;
        }

        McardThumbnail thumbnail = McardThumbnail.builder()
            .thumbnailId(existing.getThumbnailId()).mcard(mcard)
            .kakaotalkThumbnailUrl(kakaoUrl).kakaotalkPublicUrl(kakaoPublicUrl)
            .urlShareThumbnailUrl(shareUrl).urlSharePublicUrl(sharePublicUrl)
            .build();

        return McardThumbnailResponseDto.from(mcardThumbnailRepository.save(thumbnail));
    }
}
