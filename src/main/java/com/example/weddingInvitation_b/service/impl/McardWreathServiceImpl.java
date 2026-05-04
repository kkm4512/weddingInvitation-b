package com.example.weddingInvitation_b.service.impl;

import com.example.weddingInvitation_b.domain.Mcard;
import com.example.weddingInvitation_b.domain.McardWraeth;
import com.example.weddingInvitation_b.dto.request.McardWreathRequestDto;
import com.example.weddingInvitation_b.dto.response.McardWreathResponseDto;
import com.example.weddingInvitation_b.exception.EntityNotFoundException;
import com.example.weddingInvitation_b.repository.McardRepository;
import com.example.weddingInvitation_b.repository.McardWreathRepository;
import com.example.weddingInvitation_b.service.McardWreathService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 화환 보내기 서비스 구현체
 *
 * @see McardWreathService
 * @see McardWreathRepository
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class McardWreathServiceImpl implements McardWreathService {

    private final McardWreathRepository mcardWreathRepository;
    private final McardRepository mcardRepository;

    @Override
    public McardWreathResponseDto getWreath(Long mcardId) {
        McardWraeth wreath = mcardWreathRepository.findByMcardMcardId(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("화환 정보를 찾을 수 없습니다. mcardId=" + mcardId));
        return McardWreathResponseDto.from(wreath);
    }

    @Override
    @Transactional
    public McardWreathResponseDto saveWreath(Long mcardId, McardWreathRequestDto requestDto) {
        Mcard mcard = mcardRepository.findById(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("청첩장을 찾을 수 없습니다. mcardId=" + mcardId));

        McardWraeth existing = mcardWreathRepository.findByMcardMcardId(mcardId)
            .orElse(McardWraeth.builder().mcard(mcard).build());

        McardWraeth wreath = McardWraeth.builder()
            .wreathId(existing.getWreathId())
            .mcard(mcard)
            .wreathUrl(requestDto.getWreathUrl() != null ? requestDto.getWreathUrl() : existing.getWreathUrl())
            .build();

        return McardWreathResponseDto.from(mcardWreathRepository.save(wreath));
    }
}
