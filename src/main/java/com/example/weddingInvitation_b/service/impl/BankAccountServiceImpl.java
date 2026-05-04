package com.example.weddingInvitation_b.service.impl;

import com.example.weddingInvitation_b.domain.BankAccount;
import com.example.weddingInvitation_b.domain.Mcard;
import com.example.weddingInvitation_b.dto.request.BankAccountRequestDto;
import com.example.weddingInvitation_b.dto.response.BankAccountResponseDto;
import com.example.weddingInvitation_b.exception.EntityNotFoundException;
import com.example.weddingInvitation_b.repository.BankAccountRepository;
import com.example.weddingInvitation_b.repository.McardRepository;
import com.example.weddingInvitation_b.service.BankAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 계좌번호 서비스 구현체
 *
 * @see BankAccountService
 * @see BankAccountRepository
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final McardRepository mcardRepository;

    @Override
    public List<BankAccountResponseDto> getAccounts(Long mcardId) {
        return bankAccountRepository.findByMcardMcardIdOrderByDisplayOrderAsc(mcardId)
            .stream().map(BankAccountResponseDto::from).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BankAccountResponseDto addAccount(Long mcardId, BankAccountRequestDto requestDto) {
        Mcard mcard = mcardRepository.findById(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("청첩장을 찾을 수 없습니다. mcardId=" + mcardId));

        int nextOrder = requestDto.getDisplayOrder() != null
            ? requestDto.getDisplayOrder()
            : bankAccountRepository.findByMcardMcardIdOrderByDisplayOrderAsc(mcardId).size() + 1;

        BankAccount account = BankAccount.builder()
            .mcard(mcard).accountType(requestDto.getAccountType())
            .bankName(requestDto.getBankName()).accountNumber(requestDto.getAccountNumber())
            .accountHolder(requestDto.getAccountHolder()).displayOrder(nextOrder).build();

        return BankAccountResponseDto.from(bankAccountRepository.save(account));
    }

    @Override
    @Transactional
    public BankAccountResponseDto updateAccount(Long mcardId, Long accountId, BankAccountRequestDto requestDto) {
        BankAccount account = bankAccountRepository.findById(accountId)
            .orElseThrow(() -> new EntityNotFoundException("계좌를 찾을 수 없습니다. accountId=" + accountId));
        if (!account.getMcard().getMcardId().equals(mcardId))
            throw new IllegalArgumentException("해당 계좌는 요청한 청첩장에 속하지 않습니다.");

        BankAccount updated = BankAccount.builder()
            .accountId(account.getAccountId()).mcard(account.getMcard())
            .accountType(requestDto.getAccountType() != null ? requestDto.getAccountType() : account.getAccountType())
            .bankName(requestDto.getBankName() != null ? requestDto.getBankName() : account.getBankName())
            .accountNumber(requestDto.getAccountNumber() != null ? requestDto.getAccountNumber() : account.getAccountNumber())
            .accountHolder(requestDto.getAccountHolder() != null ? requestDto.getAccountHolder() : account.getAccountHolder())
            .displayOrder(requestDto.getDisplayOrder() != null ? requestDto.getDisplayOrder() : account.getDisplayOrder()).build();

        return BankAccountResponseDto.from(bankAccountRepository.save(updated));
    }

    @Override
    @Transactional
    public void deleteAccount(Long mcardId, Long accountId) {
        BankAccount account = bankAccountRepository.findById(accountId)
            .orElseThrow(() -> new EntityNotFoundException("계좌를 찾을 수 없습니다. accountId=" + accountId));
        if (!account.getMcard().getMcardId().equals(mcardId))
            throw new IllegalArgumentException("해당 계좌는 요청한 청첩장에 속하지 않습니다.");
        bankAccountRepository.delete(account);
    }
}
