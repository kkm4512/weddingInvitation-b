package com.example.weddingInvitation_b.repository;

import com.example.weddingInvitation_b.domain.Mcard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 청첩장 데이터 접근 레파지토리
 */
public interface McardRepository extends JpaRepository<Mcard, Long> {
    List<Mcard> findByUserUserIdAndIsDeletedFalse(Long userId);
    Optional<Mcard> findByInviteCode(String inviteCode);
    Optional<Mcard> findByMcardIdAndIsDeletedFalse(Long mcardId);
}

