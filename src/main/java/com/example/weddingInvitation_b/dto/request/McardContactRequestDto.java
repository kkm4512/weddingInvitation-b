package com.example.weddingInvitation_b.dto.request;
import lombok.*; import java.util.List;
/** 연락처 저장 요청 DTO */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class McardContactRequestDto {
    private List<ContactItemDto> contacts;
    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ContactItemDto {
        private Long contactId;
        private String contactType;
        private String name;
        private String phoneNumber;
        private Boolean isVisible;
    }
}
