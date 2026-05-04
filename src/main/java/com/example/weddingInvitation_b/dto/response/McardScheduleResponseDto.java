package com.example.weddingInvitation_b.dto.response;

import com.example.weddingInvitation_b.domain.McardSchedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 청첩장 예식 일시 응답 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class McardScheduleResponseDto {
    private Long scheduleId;
    private Long mcardId;
    private LocalDateTime weddingDateTime;
    private Integer prepTimeMinutes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static McardScheduleResponseDto from(McardSchedule schedule) {
        return McardScheduleResponseDto.builder()
            .scheduleId(schedule.getScheduleId())
            .mcardId(schedule.getMcard().getMcardId())
            .weddingDateTime(schedule.getWeddingDateTime())
            .prepTimeMinutes(schedule.getPrepTimeMinutes())
            .createdAt(schedule.getCreatedAt())
            .updatedAt(schedule.getUpdatedAt())
            .build();
    }
}
