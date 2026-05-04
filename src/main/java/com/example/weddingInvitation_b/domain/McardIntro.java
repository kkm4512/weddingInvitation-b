package com.example.weddingInvitation_b.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 청첩장 인트로 설정 엔티티
 *
 * <p>청첩장 상단 인트로 영역의 레이아웃 스타일을 관리한다.
 * introStyleKey는 프론트엔드와 공유하는 스타일 식별 키이다 (예: "DEFAULT", "EDGE", "OVAL").</p>
 */
@Entity
@Table(name = "mcard_intros")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class McardIntro {

    /** 인트로 설정 고유식별자 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long introId;

    /** 청첩장 */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mcard_id", nullable = false, unique = true)
    private Mcard mcard;

    /**
     * 인트로 레이아웃 스타일 키
     *
     * <p>예: DEFAULT, EDGE, OVAL, FILL, ARGE 등
     * GET /intros 응답의 styleKey 값과 일치해야 한다.</p>
     */
    @Builder.Default
    private String introStyleKey = "DEFAULT";

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
