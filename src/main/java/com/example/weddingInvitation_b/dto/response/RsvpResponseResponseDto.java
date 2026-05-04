package com.example.weddingInvitation_b.dto.response;
import com.example.weddingInvitation_b.domain.RsvpResponse;
import lombok.*;
import java.time.LocalDateTime;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class RsvpResponseResponseDto {
    private Long responseId; private Long mcardId;
    private String responderName; private String responderPhone;
    private Boolean willAttend; private Integer attendeeCount;
    private String message; private LocalDateTime respondedAt;
    public static RsvpResponseResponseDto from(RsvpResponse e) {
        return RsvpResponseResponseDto.builder()
            .responseId(e.getResponseId()).mcardId(e.getMcard().getMcardId())
            .responderName(e.getResponderName()).responderPhone(e.getResponderPhone())
            .willAttend(e.getWillAttend()).attendeeCount(e.getAttendeeCount())
            .message(e.getMessage()).respondedAt(e.getRespondedAt()).build();
    }
}
