package com.example.weddingInvitation_b.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 예식 장소 엔티티
 * 
 * <p>예식장의 이름, 주소, 지도 위치 좌표를 관리한다.
 * 하객 뷰에서 지도와 함께 표시되며, 순환 네비게이션에 사용된다.</p>
 */
@Entity
@Table(name = "mcard_venues")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class McardVenue {
    
    /** 예식장 정보 고유식별자 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long venueId;
    
    /** 청첩장 */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mcard_id", nullable = false, unique = true)
    private Mcard mcard;
    
    /** 예식장명 */
    private String venueName;
    
    /** 층/홀 정보 */
    private String floorInfo;
    
    /** 주소 */
    private String address;
    
    /** 위도 */
    private Double lat;

    /** 경도 */
    private Double lng;
    
    /** 약도 이미지 URL */
    private String mapImageUrl;
    
    /** 지도 표시 여부 */
    @Builder.Default
    private Boolean showMap = true;
    
    /** 지도 잠금 (드래그 불가) */
    @Builder.Default
    private Boolean mapLocked = false;
    
    /** 교통수단 아이콘 표시 여부 */
    @Builder.Default
    private Boolean showTransportIcons = true;
}

