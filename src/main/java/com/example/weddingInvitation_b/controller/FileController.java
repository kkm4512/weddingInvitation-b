package com.example.weddingInvitation_b.controller;

import com.example.weddingInvitation_b.dto.response.ApiResponse;
import com.example.weddingInvitation_b.dto.response.FileUploadResponseDto;
import com.example.weddingInvitation_b.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 파일 업로드 공통 컨트롤러
 *
 * <p>Cloudflare R2에 파일을 업로드하고 URL을 반환하거나 삭제하는 공통 API를 제공한다.
 * 각 섹션별 업로드(갤러리, BGM 등)는 해당 컨트롤러에서 별도로 처리한다.</p>
 *
 * @see FileService
 */
@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    /**
     * 파일 업로드 (공통)
     *
     * <p>이미지 또는 파일을 R2에 업로드한 뒤 접근 URL을 반환한다.
     * folder 파라미터로 R2 내 저장 경로를 구분한다.</p>
     *
     * @param file   업로드할 파일 (multipart/form-data)
     * @param folder 저장 폴더 (기본값: "files")
     * @return 업로드된 파일 URL
     */
    @PostMapping("/upload")
    public ApiResponse<FileUploadResponseDto> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "files") String folder) {
        String fileUrl = fileService.upload(file, folder);
        return ApiResponse.success(FileUploadResponseDto.builder().fileUrl(fileUrl).build());
    }

    /**
     * 업로드 파일 삭제
     *
     * @param fileUrl 삭제할 파일의 전체 URL (쿼리 파라미터)
     * @return 빈 성공 응답
     */
    @DeleteMapping
    public ApiResponse<Void> delete(@RequestParam String fileUrl) {
        fileService.delete(fileUrl);
        return ApiResponse.success();
    }
}
