package com.example.weddingInvitation_b.repository;

import com.example.weddingInvitation_b.domain.GuestbookMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** 방명록 메시지 레파지토리 */
public interface GuestbookMessageRepository extends JpaRepository<GuestbookMessage, Long> {

    /** 커서 없음 — 첫 페이지 (최신 ID 내림차순) */
    List<GuestbookMessage> findByMcardMcardIdAndIsDeletedFalseOrderByMessageIdDesc(Long mcardId, Pageable pageable);

    /** 커서 있음 — cursor(messageId) 미만 항목 조회 */
    List<GuestbookMessage> findByMcardMcardIdAndIsDeletedFalseAndMessageIdLessThanOrderByMessageIdDesc(Long mcardId, Long cursor, Pageable pageable);
}
