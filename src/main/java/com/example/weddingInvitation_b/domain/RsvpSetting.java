package com.example.weddingInvitation_b.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * RSVP 설정 엔티티
 * 
 * <p>청첩장의 RSVP 섹션 활성화 여부를 관리한다.</p>
 */
@Entity
@Table(name = "rsvp_settings")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RsvpSetting {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rsvpSettingId;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mcard_id", nullable = false, unique = true)
    private Mcard mcard;
    
    @Builder.Default
    private Boolean isEnabled = true;
}

