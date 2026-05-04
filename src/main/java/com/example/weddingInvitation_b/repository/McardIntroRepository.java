package com.example.weddingInvitation_b.repository;

import com.example.weddingInvitation_b.domain.McardIntro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 청첩장 인트로 설정 데이터 접근 레파지토리
 */
public interface McardIntroRepository extends JpaRepository<McardIntro, Long> {

    /**
     * 청첩장 ID로 인트로 설정 조회
     *
     * @param mcardId 청첩장 ID
     * @return 인트로 설정 (Optional)
     */
    Optional<McardIntro> findByMcardMcardId(Long mcardId);
}
