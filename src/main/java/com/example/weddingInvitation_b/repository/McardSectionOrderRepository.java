package com.example.weddingInvitation_b.repository;

import com.example.weddingInvitation_b.domain.McardSectionOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * 메뉴 순서 데이터 접근 레파지토리
 */
public interface McardSectionOrderRepository extends JpaRepository<McardSectionOrder, Long> {
    Optional<McardSectionOrder> findByMcardMcardId(Long mcardId);
}
