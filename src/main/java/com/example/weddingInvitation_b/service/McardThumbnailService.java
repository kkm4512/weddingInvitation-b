package com.example.weddingInvitation_b.service;

import com.example.weddingInvitation_b.dto.request.McardThumbnailRequestDto;
import com.example.weddingInvitation_b.dto.response.McardThumbnailResponseDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * 공유 썸네일 서비스 인터페이스
 */
public interface McardThumbnailService {
    McardThumbnailResponseDto getThumbnail(Long mcardId);
    McardThumbnailResponseDto saveThumbnail(Long mcardId, McardThumbnailRequestDto requestDto);

    /**
     * 썸네일 이미지를 R2에 업로드하고 URL을 저장한다.
     *
     * @param mcardId 청첩장 ID
     * @param file    업로드할 썸네일 이미지
     * @param type    썸네일 종류 ("kakao" | "url")
     * @return 업로드 후 저장된 썸네일 정보
     */
    McardThumbnailResponseDto uploadThumbnail(Long mcardId, MultipartFile file, String type);
}
