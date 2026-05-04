package com.example.weddingInvitation_b.dto.response;
import com.example.weddingInvitation_b.domain.GuestbookSetting;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class GuestbookSettingResponseDto {
    private Long guestbookSettingId; private Long mcardId; private Boolean isEnabled;

    public Boolean getEnabled() {
        return isEnabled;
    }

    public static GuestbookSettingResponseDto from(GuestbookSetting e) {
        return GuestbookSettingResponseDto.builder()
            .guestbookSettingId(e.getGuestbookSettingId()).mcardId(e.getMcard().getMcardId())
            .isEnabled(e.getIsEnabled()).build();
    }
}
