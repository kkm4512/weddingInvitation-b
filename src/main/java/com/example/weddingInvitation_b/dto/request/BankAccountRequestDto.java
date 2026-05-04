package com.example.weddingInvitation_b.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

/** 계좌번호 저장 요청 DTO */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class BankAccountRequestDto {
    @JsonAlias({"side"})
    private String accountType;
    private String bankName;
    private String accountNumber;
    private String accountHolder;
    private Integer displayOrder;
}
