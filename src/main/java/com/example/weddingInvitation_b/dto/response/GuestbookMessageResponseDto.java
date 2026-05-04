package com.example.weddingInvitation_b.dto.response;
import com.example.weddingInvitation_b.domain.GuestbookMessage;
import lombok.*;
import java.time.LocalDateTime;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class GuestbookMessageResponseDto {
    private Long messageId; private Long mcardId;
    private String guestName; private String content;
    private Boolean isSecret; private String replyContent;
    private LocalDateTime createdAt; private LocalDateTime repliedAt;
    public static GuestbookMessageResponseDto from(GuestbookMessage e) {
        return GuestbookMessageResponseDto.builder()
            .messageId(e.getMessageId()).mcardId(e.getMcard().getMcardId())
            .guestName(e.getGuestName()).content(e.getContent())
            .isSecret(e.getIsSecret()).replyContent(e.getReplyContent())
            .createdAt(e.getCreatedAt()).repliedAt(e.getRepliedAt()).build();
    }
}
