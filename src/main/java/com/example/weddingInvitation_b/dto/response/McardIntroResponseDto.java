package com.example.weddingInvitation_b.dto.response;

import com.example.weddingInvitation_b.domain.McardIntro;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 청첩장 인트로 설정 응답 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class McardIntroResponseDto {
    private Long introId;
    private Long mcardId;
    private String introStyleKey;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static McardIntroResponseDto from(McardIntro intro) {
        return McardIntroResponseDto.builder()
            .introId(intro.getIntroId())
            .mcardId(intro.getMcard().getMcardId())
            .introStyleKey(intro.getIntroStyleKey())
            .createdAt(intro.getCreatedAt())
            .updatedAt(intro.getUpdatedAt())
            .build();
    }
}
