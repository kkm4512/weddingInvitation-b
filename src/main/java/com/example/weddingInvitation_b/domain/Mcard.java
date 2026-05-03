package com.example.weddingInvitation_b.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 청첩장 엔티티
 * 
 * <p>사용자가 제작한 모바일 청첩장의 기본 정보를 관리한다.
 * 각 청첩장은 고유한 초대 코드를 가지며, 이를 통해 하객들이 공개 URL로 접근할 수 있다.
 * 테마, 인트로, 신랑/신부 정보 등 각 섹션의 데이터는 별도의 엔티티로 관리된다.</p>
 */
@Entity
@Table(name = "mcards")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mcard {
    
    /** 청첩장 고유식별자 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mcardId;
    
    /** 청첩장 소유자 (사용자) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    /** 청첩장 제목 */
    @Column(nullable = false)
    private String title;
    
    /** 청첩장 초대 코드 (공개 URL용) */
    @Column(nullable = false, unique = true, length = 20)
    private String inviteCode;
    
    /** 워터마크 표시 여부 (false = 결제 완료, true = 결제 없음) */
    @Column(nullable = false)
    @Builder.Default
    private Boolean hasWatermark = true;
    
    /** 예식 날짜 저장 (ISO format) */
    private LocalDateTime weddingDateTime;
    
    /** 청첩장 생성 일시 */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /** 청첩장 마지막 수정 일시 */
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    /** 청첩장 삭제 여부 (soft delete) */
    @Column(nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;

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

