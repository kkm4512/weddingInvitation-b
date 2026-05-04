package com.example.weddingInvitation_b.service.impl;

import com.example.weddingInvitation_b.domain.Mcard;
import com.example.weddingInvitation_b.domain.McardPhotoQuote;
import com.example.weddingInvitation_b.dto.request.McardPhotoQuoteRequestDto;
import com.example.weddingInvitation_b.dto.response.McardPhotoQuoteResponseDto;
import com.example.weddingInvitation_b.exception.EntityNotFoundException;
import com.example.weddingInvitation_b.repository.McardPhotoQuoteRepository;
import com.example.weddingInvitation_b.repository.McardRepository;
import com.example.weddingInvitation_b.service.FileService;
import com.example.weddingInvitation_b.service.McardPhotoQuoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 사진&글귀 서비스 구현체
 *
 * @see McardPhotoQuoteService
 * @see McardPhotoQuoteRepository
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class McardPhotoQuoteServiceImpl implements McardPhotoQuoteService {

    private final McardPhotoQuoteRepository mcardPhotoQuoteRepository;
    private final McardRepository mcardRepository;
    private final FileService fileService;

    @Override
    public McardPhotoQuoteResponseDto getPhotoQuote(Long mcardId) {
        McardPhotoQuote photoQuote = mcardPhotoQuoteRepository.findByMcardMcardId(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("사진&글귀 정보를 찾을 수 없습니다. mcardId=" + mcardId));
        return McardPhotoQuoteResponseDto.from(photoQuote);
    }

    @Override
    @Transactional
    public McardPhotoQuoteResponseDto savePhotoQuote(Long mcardId, McardPhotoQuoteRequestDto requestDto) {
        Mcard mcard = mcardRepository.findById(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("청첩장을 찾을 수 없습니다. mcardId=" + mcardId));

        McardPhotoQuote existing = mcardPhotoQuoteRepository.findByMcardMcardId(mcardId)
            .orElse(McardPhotoQuote.builder().mcard(mcard).build());

        McardPhotoQuote photoQuote = McardPhotoQuote.builder()
            .photoQuoteId(existing.getPhotoQuoteId()).mcard(mcard)
            .imageUrl(requestDto.getImageUrl() != null ? requestDto.getImageUrl() : existing.getImageUrl())
            .quoteText(requestDto.getQuoteText() != null ? requestDto.getQuoteText() : existing.getQuoteText())
            .build();

        return McardPhotoQuoteResponseDto.from(mcardPhotoQuoteRepository.save(photoQuote));
    }

    /**
     * 사진&글귀 이미지를 R2에 업로드하고 imageUrl을 저장한다 (upsert).
     *
     * <p>기존 이미지가 있으면 R2에서 삭제 후 교체한다.</p>
     *
     * @param mcardId 청첩장 ID
     * @param file    업로드할 이미지 파일
     * @return 업로드 후 저장된 사진&글귀 정보
     * @throws EntityNotFoundException 청첩장이 존재하지 않을 경우
     */
    @Override
    @Transactional
    public McardPhotoQuoteResponseDto uploadImage(Long mcardId, MultipartFile file) {
        Mcard mcard = mcardRepository.findById(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("청첩장을 찾을 수 없습니다. mcardId=" + mcardId));

        McardPhotoQuote existing = mcardPhotoQuoteRepository.findByMcardMcardId(mcardId)
            .orElse(McardPhotoQuote.builder().mcard(mcard).build());

        if (existing.getImageUrl() != null && !existing.getImageUrl().isBlank()) {
            fileService.delete(existing.getImageUrl());
        }

        String imageUrl = fileService.upload(file, "photo-quote");

        McardPhotoQuote photoQuote = McardPhotoQuote.builder()
            .photoQuoteId(existing.getPhotoQuoteId()).mcard(mcard)
            .imageUrl(imageUrl).quoteText(existing.getQuoteText())
            .build();

        return McardPhotoQuoteResponseDto.from(mcardPhotoQuoteRepository.save(photoQuote));
    }
}
