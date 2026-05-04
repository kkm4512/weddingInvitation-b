package com.example.weddingInvitation_b.dto.request;
import lombok.*;
/** 계좌번호 저장 요청 DTO */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class BankAccountRequestDto {
    private String accountType;
    private String bankName;
    private String accountNumber;
    private String accountHolder;
    private Integer displayOrder;
}
