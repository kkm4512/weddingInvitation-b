package com.example.weddingInvitation_b.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 청첩장 신랑·신부 정보 저장 요청 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class McardCoupleRequestDto {
    private String groomName;
    private String brideName;
    private String groomFatherName;
    private String groomMotherName;
    private String brideFatherName;
    private String brideMotherName;
    private Boolean groomFatherDeceased;
    private Boolean groomMotherDeceased;
    private Boolean brideFatherDeceased;
    private Boolean brideMotherDeceased;
    private Boolean showGroomContacts;
    private Boolean showBrideContacts;
}
