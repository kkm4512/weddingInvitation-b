package com.example.weddingInvitation_b.repository;

import com.example.weddingInvitation_b.domain.RsvpResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** RSVP 응답 레파지토리 */
public interface RsvpResponseRepository extends JpaRepository<RsvpResponse, Long> {

    /** 커서 없음 — 첫 페이지 (최신 ID 내림차순) */
    List<RsvpResponse> findByMcardMcardIdOrderByResponseIdDesc(Long mcardId, Pageable pageable);

    /** 커서 있음 — cursor(responseId) 미만 항목 조회 */
    List<RsvpResponse> findByMcardMcardIdAndResponseIdLessThanOrderByResponseIdDesc(Long mcardId, Long cursor, Pageable pageable);
}
