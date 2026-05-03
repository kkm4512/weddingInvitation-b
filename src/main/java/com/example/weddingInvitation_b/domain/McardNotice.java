package com.example.weddingInvitation_b.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 안내사항 엔티티
 * 
 * <p>청첩장에 표시되는 안내사항을 관리한다. (주차, 주의사항 등)</p>
 */
@Entity
@Table(name = "mcard_notices")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class McardNotice {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mcard_id", nullable = false)
    private Mcard mcard;
    
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    private String imageUrl;
    
    @Builder.Default
    private Integer displayOrder = 0;
}

