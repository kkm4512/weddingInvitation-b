package com.example.weddingInvitation_b.repository;

import com.example.weddingInvitation_b.domain.McardGreeting;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * 모시는 글(인사말) 데이터 접근 레파지토리
 */
public interface McardGreetingRepository extends JpaRepository<McardGreeting, Long> {
    Optional<McardGreeting> findByMcardMcardId(Long mcardId);
}
