package com.example.weddingInvitation_b.service;

import com.example.weddingInvitation_b.dto.request.McardBgmRequestDto;
import com.example.weddingInvitation_b.dto.response.McardBgmResponseDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * 배경음악 서비스 인터페이스
 */
public interface McardBgmService {
    McardBgmResponseDto getBgm(Long mcardId);
    McardBgmResponseDto saveBgm(Long mcardId, McardBgmRequestDto requestDto);

    /**
     * 배경음악 파일을 R2에 업로드하고 URL을 저장한다.
     *
     * @param mcardId 청첩장 ID
     * @param file    업로드할 음악 파일
     * @return 업로드 후 저장된 BGM 정보
     */
    McardBgmResponseDto uploadBgm(Long mcardId, MultipartFile file);
}
