package com.example.weddingInvitation_b.service.impl;

import com.example.weddingInvitation_b.domain.Mcard;
import com.example.weddingInvitation_b.domain.McardCouple;
import com.example.weddingInvitation_b.dto.request.McardCoupleRequestDto;
import com.example.weddingInvitation_b.dto.response.McardCoupleResponseDto;
import com.example.weddingInvitation_b.exception.EntityNotFoundException;
import com.example.weddingInvitation_b.repository.McardCoupleRepository;
import com.example.weddingInvitation_b.repository.McardRepository;
import com.example.weddingInvitation_b.service.McardCoupleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 청첩장 신랑·신부 정보 서비스 구현체
 *
 * <p>청첩장의 신랑·신부 정보를 조회하고 저장한다.</p>
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class McardCoupleServiceImpl implements McardCoupleService {

    private final McardCoupleRepository mcardCoupleRepository;
    private final McardRepository mcardRepository;

    /**
     * 청첩장 신랑·신부 정보 조회
     *
     * @param mcardId 청첩장 ID
     * @return 신랑·신부 정보
     * @throws EntityNotFoundException 정보가 없을 경우
     */
    @Override
    public McardCoupleResponseDto getCouple(Long mcardId) {
        McardCouple couple = mcardCoupleRepository.findByMcardMcardId(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("신랑·신부 정보를 찾을 수 없습니다. mcardId=" + mcardId));
        return McardCoupleResponseDto.from(couple);
    }

    /**
     * 청첩장 신랑·신부 정보 저장
     *
     * @param mcardId 청첩장 ID
     * @param requestDto 저장할 정보
     * @return 저장된 정보
     * @throws EntityNotFoundException 청첩장이 없을 경우
     */
    @Override
    @Transactional
    public McardCoupleResponseDto saveCouple(Long mcardId, McardCoupleRequestDto requestDto) {
        Mcard mcard = mcardRepository.findById(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("청첩장을 찾을 수 없습니다. mcardId=" + mcardId));

        McardCouple couple = mcardCoupleRepository.findByMcardMcardId(mcardId)
            .orElse(McardCouple.builder().mcard(mcard).build());

        couple = McardCouple.builder()
            .coupleId(couple.getCoupleId())
            .mcard(mcard)
            .groomName(requestDto.getGroomName())
            .brideName(requestDto.getBrideName())
            .groomFatherName(requestDto.getGroomFatherName())
            .groomMotherName(requestDto.getGroomMotherName())
            .brideFatherName(requestDto.getBrideFatherName())
            .brideMotherName(requestDto.getBrideMotherName())
            .groomFatherDeceased(requestDto.getGroomFatherDeceased())
            .groomMotherDeceased(requestDto.getGroomMotherDeceased())
            .brideFatherDeceased(requestDto.getBrideFatherDeceased())
            .brideMotherDeceased(requestDto.getBrideMotherDeceased())
            .showGroomContacts(requestDto.getShowGroomContacts())
            .showBrideContacts(requestDto.getShowBrideContacts())
            .build();

        McardCouple savedCouple = mcardCoupleRepository.save(couple);
        return McardCoupleResponseDto.from(savedCouple);
    }
}
