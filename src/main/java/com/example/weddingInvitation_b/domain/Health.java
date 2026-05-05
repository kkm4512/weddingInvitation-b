package com.example.weddingInvitation_b.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 헬스 체크 엔티티
 *
 * <p>DB 연결 유지를 위한 헬스 체크 레코드를 저장한다.
 * /health 엔드포인트 호출 시 이 테이블에 SELECT 쿼리를 실행하여
 * 실제 DB 커넥션이 살아있음을 보장하고, Aiven 무료 티어의 비활성 자동 종료를 방지한다.</p>
 *
 * @see HealthRepository
 * @see com.example.weddingInvitation_b.service.HealthService
 */
@Entity
@Table(name = "health")
@Getter
@NoArgsConstructor
public class Health {

    /** 헬스 체크 레코드 고유 식별자 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 헬스 체크 수행 일시 */
    @Column(name = "checked_at", nullable = false, updatable = false)
    private LocalDateTime checkedAt;

    @PrePersist
    private void onPrePersist() {
        this.checkedAt = LocalDateTime.now();
    }
}
