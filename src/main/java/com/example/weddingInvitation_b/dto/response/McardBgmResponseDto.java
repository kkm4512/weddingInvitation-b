package com.example.weddingInvitation_b.dto.response;

import com.example.weddingInvitation_b.domain.McardBgm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 배경음악 응답 DTO
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class McardBgmResponseDto {
    private Long bgmId;
    private Long mcardId;
    private String bgmUrl;
    private String publicUrl;
    private String bgmTitle;
    private Boolean autoPlay;

    public static McardBgmResponseDto from(McardBgm entity) {
        return McardBgmResponseDto.builder()
            .bgmId(entity.getBgmId())
            .mcardId(entity.getMcard().getMcardId())
            .bgmUrl(entity.getBgmUrl())
            .publicUrl(entity.getPublicUrl() != null ? entity.getPublicUrl() : entity.getBgmUrl())
            .bgmTitle(entity.getBgmTitle())
            .autoPlay(entity.getAutoPlay())
            .build();
    }
}
