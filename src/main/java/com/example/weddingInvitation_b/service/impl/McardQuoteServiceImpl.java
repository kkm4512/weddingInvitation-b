package com.example.weddingInvitation_b.service.impl;

import com.example.weddingInvitation_b.domain.Mcard;
import com.example.weddingInvitation_b.domain.McardQuote;
import com.example.weddingInvitation_b.dto.request.McardQuoteRequestDto;
import com.example.weddingInvitation_b.dto.response.McardQuoteResponseDto;
import com.example.weddingInvitation_b.exception.EntityNotFoundException;
import com.example.weddingInvitation_b.repository.McardQuoteRepository;
import com.example.weddingInvitation_b.repository.McardRepository;
import com.example.weddingInvitation_b.service.McardQuoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 글귀 서비스 구현체
 *
 * @see McardQuoteService
 * @see McardQuoteRepository
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class McardQuoteServiceImpl implements McardQuoteService {

    private final McardQuoteRepository mcardQuoteRepository;
    private final McardRepository mcardRepository;

    @Override
    public McardQuoteResponseDto getQuote(Long mcardId) {
        McardQuote quote = mcardQuoteRepository.findByMcardMcardId(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("글귀 정보를 찾을 수 없습니다. mcardId=" + mcardId));
        return McardQuoteResponseDto.from(quote);
    }

    @Override
    @Transactional
    public McardQuoteResponseDto saveQuote(Long mcardId, McardQuoteRequestDto requestDto) {
        Mcard mcard = mcardRepository.findById(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("청첩장을 찾을 수 없습니다. mcardId=" + mcardId));

        McardQuote existing = mcardQuoteRepository.findByMcardMcardId(mcardId)
            .orElse(McardQuote.builder().mcard(mcard).build());

        McardQuote quote = McardQuote.builder()
            .quoteId(existing.getQuoteId())
            .mcard(mcard)
            .quoteContent(requestDto.getQuoteContent() != null ? requestDto.getQuoteContent() : existing.getQuoteContent())
            .fontSize(requestDto.getFontSize() != null ? requestDto.getFontSize() : existing.getFontSize())
            .build();

        return McardQuoteResponseDto.from(mcardQuoteRepository.save(quote));
    }
}
