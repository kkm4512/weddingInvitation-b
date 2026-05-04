package com.example.weddingInvitation_b.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class RsvpResponseRequestDto {
    @JsonAlias({"name"})
    private String responderName;

    @JsonAlias({"phone", "phoneNumber"})
    private String responderPhone;

    @JsonAlias({"attending", "attend"})
    private Boolean willAttend;

    @JsonAlias({"headcount", "attendeeCount"})
    private Integer attendeeCount;

    @JsonAlias({"message", "content"})
    private String message;
}
