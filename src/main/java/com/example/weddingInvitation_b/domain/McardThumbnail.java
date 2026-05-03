package com.example.weddingInvitation_b.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 공유 썸네일 엔티티
 * 
 * <p>카카오톡 공유 시 표시되는 썸네일 이미지를 관리한다.</p>
 */
@Entity
@Table(name = "mcard_thumbnails")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class McardThumbnail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long thumbnailId;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mcard_id", nullable = false, unique = true)
    private Mcard mcard;
    
    private String kakaotalkThumbnailUrl;
    private String urlShareThumbnailUrl;
}

