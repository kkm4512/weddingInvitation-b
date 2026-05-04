package com.example.weddingInvitation_b.repository;

import com.example.weddingInvitation_b.domain.McardNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * 안내사항 데이터 접근 레파지토리
 */
public interface McardNoticeRepository extends JpaRepository<McardNotice, Long> {
    List<McardNotice> findByMcardMcardIdOrderByDisplayOrderAsc(Long mcardId);
}
