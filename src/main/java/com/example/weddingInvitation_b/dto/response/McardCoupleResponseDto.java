package com.example.weddingInvitation_b.dto.response;

import com.example.weddingInvitation_b.domain.McardCouple;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 청첩장 신랑·신부 정보 응답 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class McardCoupleResponseDto {
    private Long coupleId;
    private Long mcardId;
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static McardCoupleResponseDto from(McardCouple couple) {
        return McardCoupleResponseDto.builder()
            .coupleId(couple.getCoupleId())
            .mcardId(couple.getMcard().getMcardId())
            .groomName(couple.getGroomName())
            .brideName(couple.getBrideName())
            .groomFatherName(couple.getGroomFatherName())
            .groomMotherName(couple.getGroomMotherName())
            .brideFatherName(couple.getBrideFatherName())
            .brideMotherName(couple.getBrideMotherName())
            .groomFatherDeceased(couple.getGroomFatherDeceased())
            .groomMotherDeceased(couple.getGroomMotherDeceased())
            .brideFatherDeceased(couple.getBrideFatherDeceased())
            .brideMotherDeceased(couple.getBrideMotherDeceased())
            .showGroomContacts(couple.getShowGroomContacts())
            .showBrideContacts(couple.getShowBrideContacts())
            .createdAt(couple.getCreatedAt())
            .updatedAt(couple.getUpdatedAt())
            .build();
    }
}
