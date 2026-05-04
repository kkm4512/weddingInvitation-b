package com.example.weddingInvitation_b.dto.response;

import com.example.weddingInvitation_b.domain.BankAccount;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/** 계좌번호 응답 DTO */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class BankAccountResponseDto {
    private Long accountId;
    private Long mcardId;
    @JsonProperty("side")
    private String accountType;
    private String bankName;
    private String accountNumber;
    private String accountHolder;
    private Integer displayOrder;
    public static BankAccountResponseDto from(BankAccount e) {
        return BankAccountResponseDto.builder()
            .accountId(e.getAccountId()).mcardId(e.getMcard().getMcardId())
            .accountType(e.getAccountType()).bankName(e.getBankName())
            .accountNumber(e.getAccountNumber()).accountHolder(e.getAccountHolder())
            .displayOrder(e.getDisplayOrder()).build();
    }
}
