package com.example.weddingInvitation_b.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 갤러리 사진 엔티티
 * 
 * <p>청첩장 갤러리에 포함되는 사진 정보를 관리한다.
 * 각 사진은 Cloudflare에 저장되며, URL 참조와 표시 순서를 저장한다.</p>
 */
@Entity
@Table(name = "gallery_photos")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GalleryPhoto {
    
    /** 갤러리 사진 고유식별자 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoId;
    
    /** 청첩장 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mcard_id", nullable = false)
    private Mcard mcard;
    
    /** 사진 이미지 URL (Cloudflare CDN) */
    @Column(nullable = false)
    private String imageUrl;
    
    /** 사진 표시 순서 */
    private Integer displayOrder;
    
    /** 갤러리 레이아웃 (grid, carousel 등) */
    @Builder.Default
    private String layoutType = "grid";
}

