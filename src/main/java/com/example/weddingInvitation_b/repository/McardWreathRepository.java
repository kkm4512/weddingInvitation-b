package com.example.weddingInvitation_b.repository;

import com.example.weddingInvitation_b.domain.McardWraeth;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * 화환 보내기 데이터 접근 레파지토리
 */
public interface McardWreathRepository extends JpaRepository<McardWraeth, Long> {
    Optional<McardWraeth> findByMcardMcardId(Long mcardId);
}
