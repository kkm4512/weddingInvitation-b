package com.example.weddingInvitation_b.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 섹션 노출 순서 엔티티
 * 
 * <p>청첩장의 각 섹션 표시 순서를 관리한다.</p>
 */
@Entity
@Table(name = "mcard_section_orders")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class McardSectionOrder {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sectionOrderId;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mcard_id", nullable = false, unique = true)
    private Mcard mcard;
    
    /** 각 섹션의 순서를 JSON 형태로 저장 */
    @Column(columnDefinition = "JSON")
    private String sectionOrder;
}

