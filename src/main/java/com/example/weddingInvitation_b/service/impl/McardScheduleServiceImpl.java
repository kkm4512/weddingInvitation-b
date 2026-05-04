package com.example.weddingInvitation_b.service.impl;

import com.example.weddingInvitation_b.domain.Mcard;
import com.example.weddingInvitation_b.domain.McardSchedule;
import com.example.weddingInvitation_b.dto.request.McardScheduleRequestDto;
import com.example.weddingInvitation_b.dto.response.McardScheduleResponseDto;
import com.example.weddingInvitation_b.exception.EntityNotFoundException;
import com.example.weddingInvitation_b.repository.McardRepository;
import com.example.weddingInvitation_b.repository.McardScheduleRepository;
import com.example.weddingInvitation_b.service.McardScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 청첩장 예식 일시 서비스 구현체
 *
 * <p>청첩장의 예식 일시를 조회하고 저장한다.
 * McardSchedule은 청첩장당 1개만 존재한다 (OneToOne).</p>
 *
 * @see McardScheduleService
 * @see McardScheduleRepository
 * @see McardRepository
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class McardScheduleServiceImpl implements McardScheduleService {

    private final McardScheduleRepository mcardScheduleRepository;
    private final McardRepository mcardRepository;

    /**
     * 청첩장 예식 일시 단건 조회
     *
     * @param mcardId 청첩장 ID
     * @return 예식 일시 정보
     * @throws EntityNotFoundException 예식 일시 정보가 없을 경우
     */
    @Override
    public McardScheduleResponseDto getSchedule(Long mcardId) {
        McardSchedule schedule = mcardScheduleRepository.findByMcardMcardId(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("예식 일시 정보를 찾을 수 없습니다. mcardId=" + mcardId));
        return McardScheduleResponseDto.from(schedule);
    }

    /**
     * 청첩장 예식 일시 저장 (upsert)
     *
     * <p>예식 일시 정보가 없으면 새로 생성하고, 이미 있으면 덮어쓴다.
     * prepTimeMinutes가 null이면 기존 값을 유지한다.</p>
     *
     * @param mcardId    청첩장 ID
     * @param requestDto 저장할 예식 일시 정보
     * @return 저장된 예식 일시 정보
     * @throws EntityNotFoundException 청첩장이 존재하지 않을 경우
     */
    @Override
    @Transactional
    public McardScheduleResponseDto saveSchedule(Long mcardId, McardScheduleRequestDto requestDto) {
        Mcard mcard = mcardRepository.findById(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("청첩장을 찾을 수 없습니다. mcardId=" + mcardId));

        // 기존 스케줄이 있으면 ID를 유지하고 덮어쓰기, 없으면 새로 생성
        McardSchedule existing = mcardScheduleRepository.findByMcardMcardId(mcardId)
            .orElse(McardSchedule.builder().mcard(mcard).build());

        McardSchedule schedule = McardSchedule.builder()
            .scheduleId(existing.getScheduleId())
            .mcard(mcard)
            .weddingDateTime(requestDto.getWeddingDateTime())
            .prepTimeMinutes(requestDto.getPrepTimeMinutes() != null ? requestDto.getPrepTimeMinutes() : existing.getPrepTimeMinutes())
            .build();

        McardSchedule savedSchedule = mcardScheduleRepository.save(schedule);
        return McardScheduleResponseDto.from(savedSchedule);
    }
}
