package com.example.weddingInvitation_b.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 청첩장 인사말 엔티티
 * 
 * <p>신랑신부가 하객들에게 전하는 청첩 인사말 텍스트를 관리한다.
 * 제목, 본문, 관련 사진 등을 저장한다.</p>
 */
@Entity
@Table(name = "mcard_greetings")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class McardGreeting {
    
    /** 인사말 고유식별자 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long greetingId;
    
    /** 청첩장 */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mcard_id", nullable = false, unique = true)
    private Mcard mcard;
    
    /** 인사말 제목 */
    private String title;
    
    /** 인사말 본문 */
    @Column(columnDefinition = "LONGTEXT")
    private String content;
    
    /** 인사말 관련 이미지 URL */
    private String imageUrl;
    
    /** 글자 크기 (예: small, medium, large) */
    @Builder.Default
    private String fontSize = "medium";
}

