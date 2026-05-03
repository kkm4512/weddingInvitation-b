package com.example.weddingInvitation_b.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 계좌번호 정보 엔티티
 * 
 * <p>신랑신부 또는 혼주의 계좌번호를 관리한다.
 * 하객이 축의금을 송금할 때 참고할 수 있도록 제공된다.</p>
 */
@Entity
@Table(name = "bank_accounts")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccount {
    
    /** 계좌 고유식별자 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;
    
    /** 청첩장 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mcard_id", nullable = false)
    private Mcard mcard;
    
    /** 계좌 소유자 타입 (groom, bride) */
    @Column(nullable = false)
    private String accountType;
    
    /** 은행명 */
    @Column(nullable = false)
    private String bankName;
    
    /** 계좌번호 */
    @Column(nullable = false)
    private String accountNumber;
    
    /** 예금주명 */
    @Column(nullable = false)
    private String accountHolder;
    
    /** 표시 순서 */
    @Builder.Default
    private Integer displayOrder = 0;
}

