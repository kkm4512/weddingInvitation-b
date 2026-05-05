package com.example.weddingInvitation_b.repository;

import com.example.weddingInvitation_b.domain.McardNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

/**
 * 안내사항 데이터 접근 레파지토리
 */
public interface McardNoticeRepository extends JpaRepository<McardNotice, Long> {
    List<McardNotice> findByMcardMcardIdOrderByDisplayOrderAsc(Long mcardId);

    /**
     * 현재 안내사항 목록의 최대 순서 값 조회 (다음 순서 자동 부여용)
     */
    @Query("SELECT MAX(n.displayOrder) FROM McardNotice n WHERE n.mcard.mcardId = :mcardId")
    Optional<Integer> findMaxDisplayOrderByMcardId(Long mcardId);
}
