package com.example.weddingInvitation_b.service;
import com.example.weddingInvitation_b.dto.request.BankAccountRequestDto;
import com.example.weddingInvitation_b.dto.response.BankAccountResponseDto;
import java.util.List;
/** 계좌번호 서비스 인터페이스 */
public interface BankAccountService {
    List<BankAccountResponseDto> getAccounts(Long mcardId);
    BankAccountResponseDto addAccount(Long mcardId, BankAccountRequestDto requestDto);
    BankAccountResponseDto updateAccount(Long mcardId, Long accountId, BankAccountRequestDto requestDto);
    void deleteAccount(Long mcardId, Long accountId);
}
