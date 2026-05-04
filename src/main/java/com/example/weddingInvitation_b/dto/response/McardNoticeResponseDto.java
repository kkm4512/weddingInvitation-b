package com.example.weddingInvitation_b.dto.response;
import com.example.weddingInvitation_b.domain.McardNotice;
import lombok.*;
/** 안내사항 응답 DTO */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class McardNoticeResponseDto {
    private Long noticeId;
    private Long mcardId;
    private String title;
    private String content;
    private String imageUrl;
    private Integer displayOrder;
    public static McardNoticeResponseDto from(McardNotice e) {
        return McardNoticeResponseDto.builder()
            .noticeId(e.getNoticeId()).mcardId(e.getMcard().getMcardId())
            .title(e.getTitle()).content(e.getContent())
            .imageUrl(e.getImageUrl()).displayOrder(e.getDisplayOrder()).build();
    }
}
