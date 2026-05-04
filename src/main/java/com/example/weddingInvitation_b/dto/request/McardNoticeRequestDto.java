package com.example.weddingInvitation_b.dto.request;
import lombok.*;
/** 안내사항 저장 요청 DTO */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class McardNoticeRequestDto {
    private String title;
    private String content;
    private String imageUrl;
    private Integer displayOrder;
}
