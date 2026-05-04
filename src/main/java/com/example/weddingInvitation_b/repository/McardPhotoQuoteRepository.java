package com.example.weddingInvitation_b.repository;

import com.example.weddingInvitation_b.domain.McardPhotoQuote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * 사진&글귀 데이터 접근 레파지토리
 */
public interface McardPhotoQuoteRepository extends JpaRepository<McardPhotoQuote, Long> {
    Optional<McardPhotoQuote> findByMcardMcardId(Long mcardId);
}
