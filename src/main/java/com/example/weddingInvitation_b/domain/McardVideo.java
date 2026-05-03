package com.example.weddingInvitation_b.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 청첩장 동영상 엔티티
 * 
 * <p>유튜브 또는 비메오 등의 동영상 URL을 관리한다.</p>
 */
@Entity
@Table(name = "mcard_videos")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class McardVideo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long videoId;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mcard_id", nullable = false, unique = true)
    private Mcard mcard;
    
    private String videoUrl;
    private String videoTitle;
}

