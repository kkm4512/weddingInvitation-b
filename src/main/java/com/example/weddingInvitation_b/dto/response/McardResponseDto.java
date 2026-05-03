package com.example.weddingInvitation_b.dto.response;

import com.example.weddingInvitation_b.domain.Mcard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 청첩장 응답 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class McardResponseDto {
    private Long mcardId;
    private String title;
    private String inviteCode;
    private Boolean hasWatermark;
    private LocalDateTime weddingDateTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static McardResponseDto from(Mcard mcard) {
        return McardResponseDto.builder()
            .mcardId(mcard.getMcardId())
            .title(mcard.getTitle())
            .inviteCode(mcard.getInviteCode())
            .hasWatermark(mcard.getHasWatermark())
            .weddingDateTime(mcard.getWeddingDateTime())
            .createdAt(mcard.getCreatedAt())
            .updatedAt(mcard.getUpdatedAt())
            .build();
    }
}

