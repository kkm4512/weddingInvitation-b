package com.example.weddingInvitation_b.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 화환 보내기 엔티티
 * 
 * <p>화환 업체 연동 URL을 관리한다.</p>
 */
@Entity
@Table(name = "mcard_wreaths")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class McardWraeth {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wreathId;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mcard_id", nullable = false, unique = true)
    private Mcard mcard;
    
    private String wreathUrl;
}

