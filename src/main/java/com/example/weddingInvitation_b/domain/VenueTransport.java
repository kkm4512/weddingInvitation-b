package com.example.weddingInvitation_b.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 교통수단 안내 엔티티
 * 
 * <p>예식장으로 가는 대중교통(지하철, 버스) 또는 자가용 안내 정보를 관리한다.
 * 하나의 예식장에 여러 개의 교통수단 안내가 등록될 수 있다.</p>
 */
@Entity
@Table(name = "venue_transports")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VenueTransport {
    
    /** 교통수단 고유식별자 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transportId;
    
    /** 예식장 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    private McardVenue venue;
    
    /** 교통수단 타입 (subway, bus, car) */
    @Column(nullable = false)
    private String transportType;
    
    /** 교통수단 안내 텍스트 */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;
    
    /** 표시 순서 */
    @Builder.Default
    private Integer displayOrder = 0;
}

