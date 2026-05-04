package com.example.weddingInvitation_b.dto.request;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class RsvpResponseRequestDto {
    private String responderName;
    private String responderPhone;
    private Boolean willAttend;
    private Integer attendeeCount;
    private String message;
}
