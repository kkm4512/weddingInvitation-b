package com.example.weddingInvitation_b.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 연락처 정보 엔티티
 * 
 * <p>신랑신부 및 혼주의 연락처(이름, 전화번호)를 관리한다.
 * 하객이 전화 연결 버튼을 통해 연락할 수 있다.</p>
 */
@Entity
@Table(name = "mcard_contacts")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class McardContact {
    
    /** 연락처 고유식별자 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contactId;
    
    /** 청첩장 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mcard_id", nullable = false)
    private Mcard mcard;
    
    /** 연락처 타입 (groom, bride, groom_father, groom_mother, bride_father, bride_mother) */
    @Column(nullable = false)
    private String contactType;
    
    /** 연락처 이름 */
    private String name;
    
    /** 전화번호 */
    private String phoneNumber;
    
    /** 표시 여부 */
    @Builder.Default
    private Boolean isVisible = true;
}

