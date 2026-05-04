package com.example.weddingInvitation_b.service.impl;

import com.example.weddingInvitation_b.domain.Mcard;
import com.example.weddingInvitation_b.domain.McardVideo;
import com.example.weddingInvitation_b.dto.request.McardVideoRequestDto;
import com.example.weddingInvitation_b.dto.response.McardVideoResponseDto;
import com.example.weddingInvitation_b.exception.EntityNotFoundException;
import com.example.weddingInvitation_b.repository.McardRepository;
import com.example.weddingInvitation_b.repository.McardVideoRepository;
import com.example.weddingInvitation_b.service.McardVideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 동영상 서비스 구현체
 *
 * <p>청첩장의 동영상 정보를 조회하고 저장(upsert)한다.</p>
 *
 * @see McardVideoService
 * @see McardVideoRepository
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class McardVideoServiceImpl implements McardVideoService {

    private final McardVideoRepository mcardVideoRepository;
    private final McardRepository mcardRepository;

    /**
     * 동영상 정보 조회
     *
     * @param mcardId 청첩장 ID
     * @return 동영상 정보
     * @throws EntityNotFoundException 동영상 정보가 없을 경우
     */
    @Override
    public McardVideoResponseDto getVideo(Long mcardId) {
        McardVideo video = mcardVideoRepository.findByMcardMcardId(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("동영상 정보를 찾을 수 없습니다. mcardId=" + mcardId));
        return McardVideoResponseDto.from(video);
    }

    /**
     * 동영상 정보 저장 (upsert)
     *
     * @param mcardId    청첩장 ID
     * @param requestDto 저장할 동영상 정보
     * @return 저장된 동영상 정보
     * @throws EntityNotFoundException 청첩장이 존재하지 않을 경우
     */
    @Override
    @Transactional
    public McardVideoResponseDto saveVideo(Long mcardId, McardVideoRequestDto requestDto) {
        Mcard mcard = mcardRepository.findById(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("청첩장을 찾을 수 없습니다. mcardId=" + mcardId));

        McardVideo existing = mcardVideoRepository.findByMcardMcardId(mcardId)
            .orElse(McardVideo.builder().mcard(mcard).build());

        McardVideo video = McardVideo.builder()
            .videoId(existing.getVideoId())
            .mcard(mcard)
            .videoUrl(requestDto.getVideoUrl() != null ? requestDto.getVideoUrl() : existing.getVideoUrl())
            .videoTitle(requestDto.getVideoTitle() != null ? requestDto.getVideoTitle() : existing.getVideoTitle())
            .build();

        return McardVideoResponseDto.from(mcardVideoRepository.save(video));
    }
}
