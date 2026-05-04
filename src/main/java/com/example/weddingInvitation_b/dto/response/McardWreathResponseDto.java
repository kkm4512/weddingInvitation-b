package com.example.weddingInvitation_b.dto.response;

import com.example.weddingInvitation_b.domain.McardWraeth;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 화환 보내기 응답 DTO
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class McardWreathResponseDto {
    private Long wreathId;
    private Long mcardId;
    private String wreathUrl;

    public static McardWreathResponseDto from(McardWraeth entity) {
        return McardWreathResponseDto.builder()
            .wreathId(entity.getWreathId())
            .mcardId(entity.getMcard().getMcardId())
            .wreathUrl(entity.getWreathUrl())
            .build();
    }
}
