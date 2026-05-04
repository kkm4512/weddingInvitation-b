package com.example.weddingInvitation_b.service.impl;

import com.example.weddingInvitation_b.domain.Mcard;
import com.example.weddingInvitation_b.domain.McardTheme;
import com.example.weddingInvitation_b.dto.request.McardThemeRequestDto;
import com.example.weddingInvitation_b.dto.response.McardThemeResponseDto;
import com.example.weddingInvitation_b.exception.EntityNotFoundException;
import com.example.weddingInvitation_b.repository.McardRepository;
import com.example.weddingInvitation_b.repository.McardThemeRepository;
import com.example.weddingInvitation_b.service.McardThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 테마 설정 서비스 구현체
 *
 * @see McardThemeService
 * @see McardThemeRepository
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class McardThemeServiceImpl implements McardThemeService {

    private final McardThemeRepository mcardThemeRepository;
    private final McardRepository mcardRepository;

    /**
     * 청첩장 테마 조회
     *
     * @param mcardId 청첩장 ID
     * @return 테마 설정 정보
     * @throws EntityNotFoundException 테마 정보가 없을 경우
     */
    @Override
    public McardThemeResponseDto getTheme(Long mcardId) {
        McardTheme theme = mcardThemeRepository.findByMcard_McardId(mcardId);
        if (theme == null) {
            throw new EntityNotFoundException("테마 정보를 찾을 수 없습니다. mcardId=" + mcardId);
        }
        return McardThemeResponseDto.from(theme);
    }

    /**
     * 청첩장 테마 저장 (upsert)
     *
     * @param mcardId    청첩장 ID
     * @param requestDto 저장할 테마 설정
     * @return 저장된 테마 정보
     * @throws EntityNotFoundException 청첩장이 존재하지 않을 경우
     */
    @Override
    @Transactional
    public McardThemeResponseDto saveTheme(Long mcardId, McardThemeRequestDto requestDto) {
        Mcard mcard = mcardRepository.findById(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("청첩장을 찾을 수 없습니다. mcardId=" + mcardId));

        McardTheme existing = mcardThemeRepository.findByMcard_McardId(mcardId);
        Long existingId = existing != null ? existing.getThemeId() : null;

        McardTheme theme = McardTheme.builder()
            .themeId(existingId)
            .mcard(mcard)
            .themeStyle(requestDto.getThemeStyle() != null ? requestDto.getThemeStyle() : (existing != null ? existing.getThemeStyle() : null))
            .color(requestDto.getColor() != null ? requestDto.getColor() : (existing != null ? existing.getColor() : null))
            .fontFamily(requestDto.getFontFamily() != null ? requestDto.getFontFamily() : (existing != null ? existing.getFontFamily() : null))
            .fontWeight(requestDto.getFontWeight() != null ? requestDto.getFontWeight() : (existing != null ? existing.getFontWeight() : null))
            .preventZoom(requestDto.getPreventZoom() != null ? requestDto.getPreventZoom() : (existing != null ? existing.getPreventZoom() : false))
            .enableScrollAnimation(requestDto.getEnableScrollAnimation() != null ? requestDto.getEnableScrollAnimation() : (existing != null ? existing.getEnableScrollAnimation() : true))
            .build();

        return McardThemeResponseDto.from(mcardThemeRepository.save(theme));
    }
}
