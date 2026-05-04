package com.example.weddingInvitation_b.dto.request;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class GuestbookMessageRequestDto {
    private String guestName;
    private String content;
    private Boolean isSecret;
}
