package com.example.weddingInvitation_b.dto.response;
import com.example.weddingInvitation_b.domain.McardContact;
import lombok.*; import java.util.List; import java.util.stream.Collectors;
/** 연락처 응답 DTO */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class McardContactResponseDto {
    private Long contactId;
    private Long mcardId;
    private String contactType;
    private String name;
    private String phoneNumber;
    private Boolean isVisible;

    public String getRole() {
        return contactType;
    }

    public String getRelation() {
        return contactType;
    }

    public static McardContactResponseDto from(McardContact e) {
        return McardContactResponseDto.builder()
            .contactId(e.getContactId()).mcardId(e.getMcard().getMcardId())
            .contactType(e.getContactType()).name(e.getName())
            .phoneNumber(e.getPhoneNumber()).isVisible(e.getIsVisible()).build();
    }
}
