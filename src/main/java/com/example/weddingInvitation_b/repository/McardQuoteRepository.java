package com.example.weddingInvitation_b.repository;

import com.example.weddingInvitation_b.domain.McardQuote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * 글귀 데이터 접근 레파지토리
 */
public interface McardQuoteRepository extends JpaRepository<McardQuote, Long> {
    Optional<McardQuote> findByMcardMcardId(Long mcardId);
}
