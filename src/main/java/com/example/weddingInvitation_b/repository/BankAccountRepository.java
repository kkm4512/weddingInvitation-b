package com.example.weddingInvitation_b.repository;

import com.example.weddingInvitation_b.domain.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * 계좌번호 데이터 접근 레파지토리
 */
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    List<BankAccount> findByMcardMcardIdOrderByDisplayOrderAsc(Long mcardId);
}
