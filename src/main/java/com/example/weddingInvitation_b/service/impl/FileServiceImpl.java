package com.example.weddingInvitation_b.service.impl;

import com.example.weddingInvitation_b.exception.FileUploadException;
import com.example.weddingInvitation_b.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

/**
 * Cloudflare R2 파일 업로드 서비스 구현체
 *
 * <p>AWS SDK v2를 사용하여 Cloudflare R2(S3 호환)에 파일을 업로드하고 삭제한다.
 * 업로드된 파일은 버킷 URL + 키 형태의 퍼블릭 URL로 접근한다.</p>
 *
 * @see FileService
 */
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final S3Client s3Client;

    @Value("${cloudflare.bucket.name}")
    private String bucketName;

    @Value("${cloudflare.bucket.url}")
    private String bucketUrl;

    /**
     * 파일을 R2에 업로드하고 접근 URL을 반환한다.
     *
     * <p>파일명은 UUID로 생성하여 중복을 방지하고 원본 확장자를 유지한다.</p>
     *
     * @param file   업로드할 파일
     * @param folder 저장할 폴더 경로 (예: "gallery", "bgm")
     * @return 업로드된 파일의 공개 접근 URL
     * @throws FileUploadException 업로드 중 오류 발생 시
     */
    @Override
    public String upload(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw new FileUploadException("업로드할 파일이 없습니다.");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = (originalFilename != null && originalFilename.contains("."))
            ? originalFilename.substring(originalFilename.lastIndexOf('.'))
            : "";

        String key = folder + "/" + UUID.randomUUID() + extension;

        try {
            PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .build();

            s3Client.putObject(putRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        } catch (IOException e) {
            throw new FileUploadException("파일 업로드 중 오류가 발생했습니다: " + e.getMessage(), e);
        }

        return bucketUrl + "/" + key;
    }

    /**
     * R2에 저장된 파일을 삭제한다.
     *
     * <p>파일 URL에서 버킷 URL prefix를 제거하여 키를 추출한다.</p>
     *
     * @param fileUrl 삭제할 파일의 전체 URL
     * @throws FileUploadException 삭제 중 오류 발생 시
     */
    @Override
    public void delete(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) {
            return;
        }

        String prefix = bucketUrl + "/";
        if (!fileUrl.startsWith(prefix)) {
            throw new FileUploadException("유효하지 않은 파일 URL입니다: " + fileUrl);
        }

        String key = fileUrl.substring(prefix.length());

        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build();

        s3Client.deleteObject(deleteRequest);
    }
}
