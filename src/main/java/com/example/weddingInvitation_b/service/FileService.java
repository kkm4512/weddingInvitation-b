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
     * byte[] 데이터를 R2에 업로드하고 접근 URL을 반환한다.
     *
     * <p>외부 API에서 받은 이미지 바이트 등 MultipartFile 없이 업로드할 때 사용한다.</p>
     *
     * @param data        업로드할 바이트 배열
     * @param folder      저장할 폴더 경로 (예: "map", "bgm")
     * @param filename    저장할 파일명 (확장자 포함, 예: "map.png")
     * @param contentType MIME 타입 (예: "image/png")
     * @return 업로드된 파일의 공개 접근 URL
     */
    String uploadBytes(byte[] data, String folder, String filename, String contentType);

    /**
     * R2에 저장된 파일을 삭제한다.
     *
     * @param fileUrl 삭제할 파일의 전체 URL
     */
    void delete(String fileUrl);
}
