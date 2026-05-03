package com.example.weddingInvitation_b.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 방명록 설정 엔티티
 * 
 * <p>청첩장의 방명록 섹션 활성화 여부를 관리한다.</p>
 */
@Entity
@Table(name = "guestbook_settings")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestbookSetting {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long guestbookSettingId;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mcard_id", nullable = false, unique = true)
    private Mcard mcard;
    
    @Builder.Default
    private Boolean isEnabled = true;
}

