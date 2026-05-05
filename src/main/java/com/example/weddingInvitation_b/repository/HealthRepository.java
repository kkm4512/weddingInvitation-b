package com.example.weddingInvitation_b.repository;

import com.example.weddingInvitation_b.domain.Health;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 헬스 체크 레파지토리
 *
 * <p>health 테이블에 대한 DB 접근을 담당한다.
 * JpaRepository의 기본 메서드(count, findAll 등)를 활용하여
 * 실제 SELECT 쿼리를 발행함으로써 DB 커넥션 활성 상태를 확인한다.</p>
 *
 * @see com.example.weddingInvitation_b.domain.Health
 */
public interface HealthRepository extends JpaRepository<Health, Long> {
}
