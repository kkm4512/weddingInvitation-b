package com.example.weddingInvitation_b.service.impl;

import com.example.weddingInvitation_b.domain.Mcard;
import com.example.weddingInvitation_b.domain.McardIntro;
import com.example.weddingInvitation_b.dto.request.McardIntroRequestDto;
import com.example.weddingInvitation_b.dto.response.McardIntroResponseDto;
import com.example.weddingInvitation_b.exception.EntityNotFoundException;
import com.example.weddingInvitation_b.repository.McardIntroRepository;
import com.example.weddingInvitation_b.repository.McardRepository;
import com.example.weddingInvitation_b.service.McardIntroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 청첩장 인트로 설정 서비스 구현체
 *
 * <p>청첩장의 인트로 레이아웃 스타일을 조회하고 저장(upsert)한다.
 * McardIntro는 청첩장당 1개만 존재한다 (OneToOne).</p>
 *
 * @see McardIntroService
 * @see McardIntroRepository
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class McardIntroServiceImpl implements McardIntroService {

    private final McardIntroRepository mcardIntroRepository;
    private final McardRepository mcardRepository;

    /**
     * 청첩장 인트로 설정 조회
     *
     * @param mcardId 청첩장 ID
     * @return 인트로 설정 정보
     * @throws EntityNotFoundException 인트로 설정이 없을 경우
     */
    @Override
    public McardIntroResponseDto getIntro(Long mcardId) {
        McardIntro intro = mcardIntroRepository.findByMcardMcardId(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("인트로 설정을 찾을 수 없습니다. mcardId=" + mcardId));
        return McardIntroResponseDto.from(intro);
    }

    /**
     * 청첩장 인트로 스타일 저장 (upsert)
     *
     * <p>인트로 설정이 없으면 새로 생성하고, 이미 있으면 덮어쓴다.</p>
     *
     * @param mcardId    청첩장 ID
     * @param requestDto 저장할 인트로 스타일 정보
     * @return 저장된 인트로 설정 정보
     * @throws EntityNotFoundException 청첩장이 존재하지 않을 경우
     */
    @Override
    @Transactional
    public McardIntroResponseDto saveIntro(Long mcardId, McardIntroRequestDto requestDto) {
        Mcard mcard = mcardRepository.findById(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("청첩장을 찾을 수 없습니다. mcardId=" + mcardId));

        McardIntro existing = mcardIntroRepository.findByMcardMcardId(mcardId)
            .orElse(McardIntro.builder().mcard(mcard).build());

        McardIntro intro = McardIntro.builder()
            .introId(existing.getIntroId())
            .mcard(mcard)
            .introStyleKey(requestDto.getIntroStyleKey() != null
                ? requestDto.getIntroStyleKey() : existing.getIntroStyleKey())
            .build();

        return McardIntroResponseDto.from(mcardIntroRepository.save(intro));
    }
}
