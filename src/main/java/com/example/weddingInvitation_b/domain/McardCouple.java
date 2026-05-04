package com.example.weddingInvitation_b.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 신랑/신부 정보 엔티티
 * 
 * <p>청첩장에 표시되는 신랑과 신부의 이름, 부모님 정보를 관리한다.</p>
 */
@Entity
@Table(name = "mcard_couples")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class McardCouple {
    
    /** 신랑/신부 정보 고유식별자 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coupleId;
    
    /** 청첩장 */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mcard_id", nullable = false, unique = true)
    private Mcard mcard;
    
    /** 신랑 이름 */
    private String groomName;
    
    /** 신부 이름 */
    private String brideName;
    
    /** 신랑 아버지 성함 */
    private String groomFatherName;
    
    /** 신랑 어머니 성함 */
    private String groomMotherName;
    
    /** 신부 아버지 성함 */
    private String brideFatherName;
    
    /** 신부 어머니 성함 */
    private String brideMotherName;
    
    /** 신랑 아버지 故 표시 여부 */
    @Builder.Default
    private Boolean groomFatherDeceased = false;
    
    /** 신랑 어머니 故 표시 여부 */
    @Builder.Default
    private Boolean groomMotherDeceased = false;
    
    /** 신부 아버지 故 표시 여부 */
    @Builder.Default
    private Boolean brideFatherDeceased = false;
    
    /** 신부 어머니 故 표시 여부 */
    @Builder.Default
    private Boolean brideMotherDeceased = false;
    
    /** 신랑측 연락처 표시 여부 */
    @Builder.Default
    private Boolean showGroomContacts = true;
    
    /** 신부측 연락처 표시 여부 */
    @Builder.Default
    private Boolean showBrideContacts = true;

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
