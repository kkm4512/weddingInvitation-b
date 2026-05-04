package com.example.weddingInvitation_b.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class GuestbookMessageRequestDto {
    @JsonAlias({"writerName"})
    private String guestName;

    @JsonAlias({"message", "content"})
    private String content;

    @JsonAlias({"isSecret", "secret"})
    private Boolean isSecret;

    private String password;
}
