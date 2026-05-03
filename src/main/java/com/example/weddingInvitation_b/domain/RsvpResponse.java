package com.example.weddingInvitation_b.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * RSVP 응답 엔티티
 * 
 * <p>하객의 참석 여부 및 참석 인원 수 응답을 관리한다.
 * 신랑신부가 하객들의 응답을 확인하고 참석자 명단을 조회할 수 있다.</p>
 */
@Entity
@Table(name = "rsvp_responses")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RsvpResponse {
    
    /** RSVP 응답 고유식별자 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long responseId;
    
    /** 청첩장 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mcard_id", nullable = false)
    private Mcard mcard;
    
    /** 응답자 이름 */
    @Column(nullable = false)
    private String responderName;
    
    /** 응답자 연락처 */
    private String responderPhone;
    
    /** 참석 여부 (true: 참석, false: 불참) */
    @Column(nullable = false)
    private Boolean willAttend;
    
    /** 참석 인원 수 */
    @Column(nullable = false, columnDefinition = "INT DEFAULT 1")
    @Builder.Default
    private Integer attendeeCount = 1;
    
    /** 응답 메시지 */
    @Column(columnDefinition = "TEXT")
    private String message;
    
    /** 응답 일시 */
    @Column(nullable = false, updatable = false)
    private LocalDateTime respondedAt;
    
    /** 응답 수정 일시 */
    private LocalDateTime updatedAt;

    @PrePersist
    private void onPrePersist() {
        this.respondedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    private void onPreUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

