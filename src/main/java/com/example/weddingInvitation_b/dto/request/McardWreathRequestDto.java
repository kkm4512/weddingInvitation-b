package com.example.weddingInvitation_b.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 화환 보내기 저장 요청 DTO
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class McardWreathRequestDto {
    private String wreathUrl;
}
