package com.example.weddingInvitation_b.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사진 + 글귀 블록 엔티티
 * 
 * <p>사진과 글귀를 함께 표시하는 블록을 관리한다.</p>
 */
@Entity
@Table(name = "mcard_photo_quotes")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class McardPhotoQuote {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoQuoteId;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mcard_id", nullable = false, unique = true)
    private Mcard mcard;
    
    private String imageUrl;
    
    @Column(columnDefinition = "TEXT")
    private String quoteText;
}

