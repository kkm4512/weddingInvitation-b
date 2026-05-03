package com.example.weddingInvitation_b.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 결제 정보 엔티티
 * 
 * <p>청첩장 워터마크 제거 관련 결제 정보를 관리한다.</p>
 */
@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mcard_id", nullable = false)
    private Mcard mcard;
    
    private Long amount;
    private String paymentMethod;
    private String transactionId;
    
    @Builder.Default
    private String status = "pending";
    
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime paidAt;

    @PrePersist
    private void onPrePersist() {
        this.createdAt = LocalDateTime.now();
    }
}

