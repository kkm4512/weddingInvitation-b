package com.example.weddingInvitation_b.repository;

import com.example.weddingInvitation_b.domain.McardContact;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * 연락처 정보 데이터 접근 레파지토리
 */
public interface McardContactRepository extends JpaRepository<McardContact, Long> {
    List<McardContact> findByMcardMcardId(Long mcardId);
}
