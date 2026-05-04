package com.example.weddingInvitation_b.repository;

import com.example.weddingInvitation_b.domain.McardVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * 동영상 데이터 접근 레파지토리
 */
public interface McardVideoRepository extends JpaRepository<McardVideo, Long> {
    Optional<McardVideo> findByMcardMcardId(Long mcardId);
}
