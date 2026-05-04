package com.example.weddingInvitation_b.dto.response;

import com.example.weddingInvitation_b.domain.McardSectionOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 메뉴 순서 응답 DTO
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class McardSectionOrderResponseDto {
    private Long sectionOrderId;
    private Long mcardId;
    private List<String> sectionOrder;

    public static McardSectionOrderResponseDto from(McardSectionOrder entity) {
        return McardSectionOrderResponseDto.builder()
            .sectionOrderId(entity.getSectionOrderId())
            .mcardId(entity.getMcard().getMcardId())
            .sectionOrder(parseJsonArray(entity.getSectionOrder()))
            .build();
    }

    /**
     * JSON 배열 문자열을 List<String>으로 파싱 (Jackson 의존성 없는 순수 Java 구현)
     * 예: ["greeting","video","gallery"] → ["greeting", "video", "gallery"]
     */
    private static List<String> parseJsonArray(String json) {
        if (json == null || json.isBlank()) return Collections.emptyList();
        String trimmed = json.trim();
        if (!trimmed.startsWith("[") || !trimmed.endsWith("]")) return Collections.emptyList();
        String inner = trimmed.substring(1, trimmed.length() - 1).trim();
        if (inner.isEmpty()) return Collections.emptyList();

        List<String> result = new ArrayList<>();
        for (String token : inner.split(",")) {
            String s = token.trim();
            if (s.startsWith("\"")) s = s.substring(1);
            if (s.endsWith("\"")) s = s.substring(0, s.length() - 1);
            if (!s.isEmpty()) result.add(s);
        }
        return result;
    }
}
