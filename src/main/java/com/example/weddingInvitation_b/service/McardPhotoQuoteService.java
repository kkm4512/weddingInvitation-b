package com.example.weddingInvitation_b.service;

import com.example.weddingInvitation_b.dto.request.McardPhotoQuoteRequestDto;
import com.example.weddingInvitation_b.dto.response.McardPhotoQuoteResponseDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * 사진&글귀 서비스 인터페이스
 */
public interface McardPhotoQuoteService {
    McardPhotoQuoteResponseDto getPhotoQuote(Long mcardId);
    McardPhotoQuoteResponseDto savePhotoQuote(Long mcardId, McardPhotoQuoteRequestDto requestDto);

    /**
     * 사진&글귀 이미지를 R2에 업로드하고 URL을 저장한다.
     *
     * @param mcardId 청첩장 ID
     * @param file    업로드할 이미지 파일
     * @return 업로드 후 저장된 사진&글귀 정보
     */
    McardPhotoQuoteResponseDto uploadImage(Long mcardId, MultipartFile file);
}
