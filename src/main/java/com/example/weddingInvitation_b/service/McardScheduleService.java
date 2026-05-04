package com.example.weddingInvitation_b.service;

import com.example.weddingInvitation_b.dto.request.McardScheduleRequestDto;
import com.example.weddingInvitation_b.dto.response.McardScheduleResponseDto;

/**
 * 청첩장 예식 일시 서비스 인터페이스
 *
 * <p>청첩장당 예식 일시는 1개만 존재한다 (OneToOne).
 * 조회와 저장(upsert)만 제공한다.</p>
 */
public interface McardScheduleService {

    /**
     * 예식 일시 조회
     *
     * @param mcardId 청첩장 ID
     * @return 예식 일시 정보
     */
    McardScheduleResponseDto getSchedule(Long mcardId);

    /**
     * 예식 일시 저장 (upsert)
     *
     * <p>없으면 생성, 있으면 덮어쓴다.</p>
     *
     * @param mcardId    청첩장 ID
     * @param requestDto 저장할 예식 일시 정보
     * @return 저장된 예식 일시 정보
     */
    McardScheduleResponseDto saveSchedule(Long mcardId, McardScheduleRequestDto requestDto);

}
