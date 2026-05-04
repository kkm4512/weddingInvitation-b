package com.example.weddingInvitation_b.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 배경음악 엔티티
 * 
 * <p>청첩장 배경음악 설정을 관리한다.</p>
 */
@Entity
@Table(name = "mcard_bgms")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class McardBgm {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bgmId;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mcard_id", nullable = false, unique = true)
    private Mcard mcard;
    
    private String bgmUrl;
    private String publicUrl;
    private String bgmTitle;
    
    @Builder.Default
    private Boolean autoPlay = false;
}

