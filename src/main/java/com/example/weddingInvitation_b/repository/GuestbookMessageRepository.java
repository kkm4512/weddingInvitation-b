package com.example.weddingInvitation_b.repository;
import com.example.weddingInvitation_b.domain.GuestbookMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
/** 방명록 메시지 레파지토리 */
public interface GuestbookMessageRepository extends JpaRepository<GuestbookMessage, Long> {
    List<GuestbookMessage> findByMcardMcardIdAndIsDeletedFalseOrderByCreatedAtDesc(Long mcardId);
}
