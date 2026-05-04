package com.example.weddingInvitation_b.dto.response;

import com.example.weddingInvitation_b.domain.McardGreeting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 모시는 글(인사말) 응답 DTO
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class McardGreetingResponseDto {
    private Long greetingId;
    private Long mcardId;
    private String title;
    private String content;
    private String imageUrl;
    private String fontSize;

    public static McardGreetingResponseDto from(McardGreeting entity) {
        return McardGreetingResponseDto.builder()
            .greetingId(entity.getGreetingId())
            .mcardId(entity.getMcard().getMcardId())
            .title(entity.getTitle())
            .content(entity.getContent())
            .imageUrl(entity.getImageUrl())
            .fontSize(entity.getFontSize())
            .build();
    }
}
