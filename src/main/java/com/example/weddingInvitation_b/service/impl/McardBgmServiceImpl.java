package com.example.weddingInvitation_b.service.impl;

import com.example.weddingInvitation_b.domain.Mcard;
import com.example.weddingInvitation_b.domain.McardBgm;
import com.example.weddingInvitation_b.dto.request.McardBgmRequestDto;
import com.example.weddingInvitation_b.dto.response.McardBgmResponseDto;
import com.example.weddingInvitation_b.exception.EntityNotFoundException;
import com.example.weddingInvitation_b.repository.McardBgmRepository;
import com.example.weddingInvitation_b.repository.McardRepository;
import com.example.weddingInvitation_b.service.FileService;
import com.example.weddingInvitation_b.service.McardBgmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 배경음악 서비스 구현체
 *
 * <p>청첩장의 배경음악 설정을 조회하고 저장(upsert)한다.
 * 파일 업로드 시 Cloudflare R2에 저장 후 URL을 bgmUrl에 저장한다.</p>
 *
 * @see McardBgmService
 * @see McardBgmRepository
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class McardBgmServiceImpl implements McardBgmService {

    private final McardBgmRepository mcardBgmRepository;
    private final McardRepository mcardRepository;
    private final FileService fileService;

    /**
     * 배경음악 설정 조회
     *
     * @param mcardId 청첩장 ID
     * @return 배경음악 정보
     * @throws EntityNotFoundException 배경음악 정보가 없을 경우
     */
    @Override
    public McardBgmResponseDto getBgm(Long mcardId) {
        McardBgm bgm = mcardBgmRepository.findByMcardMcardId(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("배경음악 정보를 찾을 수 없습니다. mcardId=" + mcardId));
        return McardBgmResponseDto.from(bgm);
    }

    /**
     * 배경음악 설정 저장 (upsert)
     *
     * @param mcardId    청첩장 ID
     * @param requestDto 저장할 배경음악 정보
     * @return 저장된 배경음악 정보
     * @throws EntityNotFoundException 청첩장이 존재하지 않을 경우
     */
    @Override
    @Transactional
    public McardBgmResponseDto saveBgm(Long mcardId, McardBgmRequestDto requestDto) {
        Mcard mcard = mcardRepository.findById(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("청첩장을 찾을 수 없습니다. mcardId=" + mcardId));

        McardBgm existing = mcardBgmRepository.findByMcardMcardId(mcardId)
            .orElse(McardBgm.builder().mcard(mcard).build());

        McardBgm bgm = McardBgm.builder()
            .bgmId(existing.getBgmId()).mcard(mcard)
            .bgmUrl(requestDto.getBgmUrl() != null ? requestDto.getBgmUrl() : existing.getBgmUrl())
            .bgmTitle(requestDto.getBgmTitle() != null ? requestDto.getBgmTitle() : existing.getBgmTitle())
            .autoPlay(requestDto.getAutoPlay() != null ? requestDto.getAutoPlay() : existing.getAutoPlay())
            .build();

        return McardBgmResponseDto.from(mcardBgmRepository.save(bgm));
    }

    /**
     * 배경음악 파일 업로드 후 URL 저장 (upsert)
     *
     * <p>파일을 R2에 업로드하고 반환된 URL을 bgmUrl에 저장한다.
     * 기존 파일이 있으면 R2에서 삭제 후 교체한다.</p>
     *
     * @param mcardId 청첩장 ID
     * @param file    업로드할 음악 파일
     * @return 업로드 후 저장된 BGM 정보
     * @throws EntityNotFoundException 청첩장이 존재하지 않을 경우
     */
    @Override
    @Transactional
    public McardBgmResponseDto uploadBgm(Long mcardId, MultipartFile file) {
        Mcard mcard = mcardRepository.findById(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("청첩장을 찾을 수 없습니다. mcardId=" + mcardId));

        McardBgm existing = mcardBgmRepository.findByMcardMcardId(mcardId)
            .orElse(McardBgm.builder().mcard(mcard).build());

        // 기존 파일이 있으면 R2에서 삭제
        if (existing.getBgmUrl() != null && !existing.getBgmUrl().isBlank()) {
            fileService.delete(existing.getBgmUrl());
        }

        String bgmUrl = fileService.upload(file, "bgm");

        McardBgm bgm = McardBgm.builder()
            .bgmId(existing.getBgmId()).mcard(mcard)
            .bgmUrl(bgmUrl)
            .bgmTitle(existing.getBgmTitle())
            .autoPlay(existing.getAutoPlay())
            .build();

        return McardBgmResponseDto.from(mcardBgmRepository.save(bgm));
    }
}
