package com.example.weddingInvitation_b.repository;

import com.example.weddingInvitation_b.domain.McardSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 청첩장 예식 일시 데이터 접근 레파지토리
 *
 * <p>JpaRepository를 상속하여 기본 CRUD를 제공한다.</p>
 */
public interface McardScheduleRepository extends JpaRepository<McardSchedule, Long> {

    /**
     * 청첩장 ID로 예식 일시 조회
     *
     * @param mcardId 청첩장 ID
     * @return 예식 일시 (Optional)
     */
    java.util.Optional<McardSchedule> findByMcardMcardId(Long mcardId);
}
