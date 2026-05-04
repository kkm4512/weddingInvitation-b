package com.example.weddingInvitation_b.repository;

import com.example.weddingInvitation_b.domain.McardVenue;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * 예식 장소 데이터 접근 레파지토리
 */
public interface McardVenueRepository extends JpaRepository<McardVenue, Long> {
    Optional<McardVenue> findByMcardMcardId(Long mcardId);
}
