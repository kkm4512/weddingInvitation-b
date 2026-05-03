package com.example.weddingInvitation_b.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 글귀 엔티티
 * 
 * <p>청첩장에 표시되는 텍스트 글귀를 관리한다.</p>
 */
@Entity
@Table(name = "mcard_quotes")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class McardQuote {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quoteId;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mcard_id", nullable = false, unique = true)
    private Mcard mcard;
    
    @Column(columnDefinition = "TEXT")
    private String quoteContent;
    
    @Builder.Default
    private String fontSize = "medium";
}

