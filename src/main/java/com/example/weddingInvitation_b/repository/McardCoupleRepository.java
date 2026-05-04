package com.example.weddingInvitation_b.repository;

import com.example.weddingInvitation_b.domain.McardCouple;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 청첩장 신랑·신부 정보 데이터 접근 레파지토리
 *
 * <p>JpaRepository를 상속하여 기본 CRUD를 제공한다.</p>
 */
public interface McardCoupleRepository extends JpaRepository<McardCouple, Long> {

    /**
     * 청첩장 ID로 신랑·신부 정보 조회
     *
     * @param mcardId 청첩장 ID
     * @return 신랑·신부 정보 (Optional)
     */
    java.util.Optional<McardCouple> findByMcardMcardId(Long mcardId);
}
