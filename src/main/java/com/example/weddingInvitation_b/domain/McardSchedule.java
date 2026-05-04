package com.example.weddingInvitation_b.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 예식 일시 엔티티
 * 
 * <p>청첩장에 표시되는 예식 날짜와 시간을 관리한다.
 * D-Day 카운트다운 계산의 기준이 된다.</p>
 */
@Entity
@Table(name = "mcard_schedules")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class McardSchedule {
    
    /** 예식 일시 고유식별자 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;
    
    /** 청첩장 */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mcard_id", nullable = false, unique = true)
    private Mcard mcard;
    
    /** 예식 일시 */
    private LocalDateTime weddingDateTime;
    
    /** 예식장 준비 시간 (분 단위) */
    @Builder.Default
    private Integer prepTimeMinutes = 30;

    /** 생성 일시 */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** 수정 일시 */
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    private void onPrePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    private void onPreUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
