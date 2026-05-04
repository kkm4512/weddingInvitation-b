package com.example.weddingInvitation_b.service.impl;

import com.example.weddingInvitation_b.domain.Mcard;
import com.example.weddingInvitation_b.domain.McardGreeting;
import com.example.weddingInvitation_b.dto.request.McardGreetingRequestDto;
import com.example.weddingInvitation_b.dto.response.McardGreetingResponseDto;
import com.example.weddingInvitation_b.exception.EntityNotFoundException;
import com.example.weddingInvitation_b.repository.McardGreetingRepository;
import com.example.weddingInvitation_b.repository.McardRepository;
import com.example.weddingInvitation_b.service.McardGreetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 모시는 글(인사말) 서비스 구현체
 *
 * <p>청첩장의 인사말을 조회하고 저장(upsert)한다.</p>
 *
 * @see McardGreetingService
 * @see McardGreetingRepository
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class McardGreetingServiceImpl implements McardGreetingService {

    private final McardGreetingRepository mcardGreetingRepository;
    private final McardRepository mcardRepository;

    /**
     * 인사말 조회
     *
     * @param mcardId 청첩장 ID
     * @return 인사말 정보
     * @throws EntityNotFoundException 인사말 정보가 없을 경우
     */
    @Override
    public McardGreetingResponseDto getGreeting(Long mcardId) {
        McardGreeting greeting = mcardGreetingRepository.findByMcardMcardId(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("인사말 정보를 찾을 수 없습니다. mcardId=" + mcardId));
        return McardGreetingResponseDto.from(greeting);
    }

    /**
     * 인사말 저장 (upsert)
     *
     * <p>없으면 생성, 있으면 덮어쓴다. null 필드는 기존 값을 유지한다.</p>
     *
     * @param mcardId    청첩장 ID
     * @param requestDto 저장할 인사말 정보
     * @return 저장된 인사말 정보
     * @throws EntityNotFoundException 청첩장이 존재하지 않을 경우
     */
    @Override
    @Transactional
    public McardGreetingResponseDto saveGreeting(Long mcardId, McardGreetingRequestDto requestDto) {
        Mcard mcard = mcardRepository.findById(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("청첩장을 찾을 수 없습니다. mcardId=" + mcardId));

        McardGreeting existing = mcardGreetingRepository.findByMcardMcardId(mcardId)
            .orElse(McardGreeting.builder().mcard(mcard).build());

        McardGreeting greeting = McardGreeting.builder()
            .greetingId(existing.getGreetingId())
            .mcard(mcard)
            .title(requestDto.getTitle() != null ? requestDto.getTitle() : existing.getTitle())
            .content(requestDto.getContent() != null ? requestDto.getContent() : existing.getContent())
            .imageUrl(requestDto.getImageUrl() != null ? requestDto.getImageUrl() : existing.getImageUrl())
            .fontSize(requestDto.getFontSize() != null ? requestDto.getFontSize() : existing.getFontSize())
            .build();

        return McardGreetingResponseDto.from(mcardGreetingRepository.save(greeting));
    }
}
