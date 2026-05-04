package com.example.weddingInvitation_b.dto.response;

import com.example.weddingInvitation_b.domain.McardVideo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 동영상 응답 DTO
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class McardVideoResponseDto {
    private Long videoId;
    private Long mcardId;
    private String videoUrl;
    private String videoTitle;

    public static McardVideoResponseDto from(McardVideo entity) {
        return McardVideoResponseDto.builder()
            .videoId(entity.getVideoId())
            .mcardId(entity.getMcard().getMcardId())
            .videoUrl(entity.getVideoUrl())
            .videoTitle(entity.getVideoTitle())
            .build();
    }
}
