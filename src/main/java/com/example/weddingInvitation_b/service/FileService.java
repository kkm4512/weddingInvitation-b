package com.example.weddingInvitation_b.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 파일 업로드 서비스 인터페이스
 *
 * <p>Cloudflare R2에 파일을 업로드하고 삭제하는 기능을 정의한다.</p>
 */
public interface FileService {

    /**
     * 파일을 R2에 업로드하고 접근 URL을 반환한다.
     *
     * @param file   업로드할 파일
     * @param folder 저장할 폴더 경로 (예: "gallery", "bgm", "thumbnail")
     * @return 업로드된 파일의 공개 접근 URL
     */
    String upload(MultipartFile file, String folder);

    /**
     * R2에 저장된 파일을 삭제한다.
     *
     * @param fileUrl 삭제할 파일의 전체 URL
     */
    void delete(String fileUrl);
}
