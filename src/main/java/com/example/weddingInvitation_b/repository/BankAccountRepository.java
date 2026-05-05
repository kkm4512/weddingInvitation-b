package com.example.weddingInvitation_b.repository;

import com.example.weddingInvitation_b.domain.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

/**
 * 계좌번호 데이터 접근 레파지토리
 */
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    List<BankAccount> findByMcardMcardIdOrderByDisplayOrderAsc(Long mcardId);

    /**
     * 현재 계좌 목록의 최대 순서 값 조회 (다음 순서 자동 부여용)
     */
    @Query("SELECT MAX(a.displayOrder) FROM BankAccount a WHERE a.mcard.mcardId = :mcardId")
    Optional<Integer> findMaxDisplayOrderByMcardId(Long mcardId);
}
