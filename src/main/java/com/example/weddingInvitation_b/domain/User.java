package com.example.weddingInvitation_b.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 사용자 엔티티
 * 
 * <p>카카오, 구글, 네이버 등 소셜 로그인으로 가입한 사용자 정보를 관리한다.
 * JWT 기반 인증으로 사용자를 식별하며, 청첩장 정보의 주인으로 연관된다.</p>
 */
@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    
    /** 사용자 고유식별자 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    
    /** 소셜 로그인 제공자 (kakao, google, naver 등) */
    @Column(nullable = false)
    private String provider;
    
    /** 소셜 로그인 제공자의 사용자 ID */
    @Column(nullable = false, unique = true)
    private String providerId;
    
    /** 사용자 닉네임 또는 이름 */
    @Column(nullable = false)
    private String name;
    
    /** 사용자 이메일 */
    @Column(unique = true)
    private String email;
    
    /** 사용자 프로필 이미지 URL */
    private String profileImageUrl;
    
    /** 계정 생성 일시 */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /** 계정 마지막 수정 일시 */
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    /** 계정 삭제 여부 (soft delete) */
    @Column(nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;

    @PrePersist
    private void onPrePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    private void onPreUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

