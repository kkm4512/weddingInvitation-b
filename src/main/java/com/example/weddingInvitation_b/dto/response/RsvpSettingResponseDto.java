package com.example.weddingInvitation_b.dto.response;
import com.example.weddingInvitation_b.domain.RsvpSetting;
import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class RsvpSettingResponseDto {
    private Long rsvpSettingId; private Long mcardId; private Boolean isEnabled;

    public Boolean getEnabled() {
        return isEnabled;
    }

    public static RsvpSettingResponseDto from(RsvpSetting e) {
        return RsvpSettingResponseDto.builder()
            .rsvpSettingId(e.getRsvpSettingId()).mcardId(e.getMcard().getMcardId())
            .isEnabled(e.getIsEnabled()).build();
    }
}
