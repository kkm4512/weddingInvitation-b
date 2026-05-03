package com.example.weddingInvitation_b.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 방명록 메시지 엔티티
 * 
 * <p>하객이 작성한 축하 메시지와 신랑신부의 답글을 관리한다.
 * 비밀글 설정, 외부 공개 여부 등을 제어할 수 있다.</p>
 */
@Entity
@Table(name = "guestbook_messages")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestbookMessage {
    
    /** 방명록 메시지 고유식별자 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;
    
    /** 청첩장 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mcard_id", nullable = false)
    private Mcard mcard;
    
    /** 메시지 작성자 이름 */
    @Column(nullable = false)
    private String guestName;
    
    /** 메시지 내용 */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    /** 메시지 비밀글 여부 */
    @Builder.Default
    private Boolean isSecret = false;
    
    /** 신랑신부 답글 내용 */
    @Column(columnDefinition = "TEXT")
    private String replyContent;
    
    /** 메시지 생성 일시 */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /** 답글 생성 일시 */
    private LocalDateTime repliedAt;
    
    /** 메시지 삭제 여부 */
    @Builder.Default
    private Boolean isDeleted = false;

    @PrePersist
    private void onPrePersist() {
        this.createdAt = LocalDateTime.now();
    }
}

