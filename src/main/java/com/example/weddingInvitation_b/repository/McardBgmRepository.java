package com.example.weddingInvitation_b.repository;

import com.example.weddingInvitation_b.domain.McardBgm;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * 배경음악 데이터 접근 레파지토리
 */
public interface McardBgmRepository extends JpaRepository<McardBgm, Long> {
    Optional<McardBgm> findByMcardMcardId(Long mcardId);
}
