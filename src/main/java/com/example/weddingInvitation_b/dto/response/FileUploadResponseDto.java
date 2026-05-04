package com.example.weddingInvitation_b.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 파일 업로드 응답 DTO
 *
 * <p>Cloudflare R2에 업로드된 파일의 접근 URL을 반환한다.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileUploadResponseDto {

    /** 업로드된 파일의 접근 URL (Cloudflare R2 퍼블릭 URL) */
    private String fileUrl;
}
