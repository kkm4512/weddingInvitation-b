package com.example.weddingInvitation_b.repository;

import com.example.weddingInvitation_b.domain.McardThumbnail;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * 공유 썸네일 데이터 접근 레파지토리
 */
public interface McardThumbnailRepository extends JpaRepository<McardThumbnail, Long> {
    Optional<McardThumbnail> findByMcardMcardId(Long mcardId);
}
