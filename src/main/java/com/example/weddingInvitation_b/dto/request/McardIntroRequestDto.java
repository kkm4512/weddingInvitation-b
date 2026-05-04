package com.example.weddingInvitation_b.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 청첩장 인트로 스타일 저장 요청 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class McardIntroRequestDto {
    private String introStyleKey;
}
