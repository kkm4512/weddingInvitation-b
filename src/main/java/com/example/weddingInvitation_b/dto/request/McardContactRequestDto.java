package com.example.weddingInvitation_b.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;
import java.util.List;

/** 연락처 저장 요청 DTO */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class McardContactRequestDto {
    private List<ContactItemDto> contacts;

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ContactItemDto {
        private Long contactId;

        @JsonAlias({"type", "role", "relation"})
        private String contactType;

        private String name;

        @JsonAlias({"phone", "phoneNumber"})
        private String phoneNumber;

        private Boolean isVisible;
    }
}
