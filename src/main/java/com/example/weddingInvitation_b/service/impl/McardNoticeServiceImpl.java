package com.example.weddingInvitation_b.service.impl;

import com.example.weddingInvitation_b.domain.Mcard;
import com.example.weddingInvitation_b.domain.McardNotice;
import com.example.weddingInvitation_b.dto.request.McardNoticeRequestDto;
import com.example.weddingInvitation_b.dto.response.McardNoticeResponseDto;
import com.example.weddingInvitation_b.exception.EntityNotFoundException;
import com.example.weddingInvitation_b.repository.McardNoticeRepository;
import com.example.weddingInvitation_b.repository.McardRepository;
import com.example.weddingInvitation_b.service.McardNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 안내사항 서비스 구현체
 *
 * @see McardNoticeService
 * @see McardNoticeRepository
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class McardNoticeServiceImpl implements McardNoticeService {

    private final McardNoticeRepository mcardNoticeRepository;
    private final McardRepository mcardRepository;

    @Override
    public List<McardNoticeResponseDto> getNotices(Long mcardId) {
        return mcardNoticeRepository.findByMcardMcardIdOrderByDisplayOrderAsc(mcardId)
            .stream().map(McardNoticeResponseDto::from).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public McardNoticeResponseDto addNotice(Long mcardId, McardNoticeRequestDto requestDto) {
        Mcard mcard = mcardRepository.findById(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("청첩장을 찾을 수 없습니다. mcardId=" + mcardId));

        int nextOrder = requestDto.getDisplayOrder() != null
            ? requestDto.getDisplayOrder()
            : mcardNoticeRepository.findByMcardMcardIdOrderByDisplayOrderAsc(mcardId).size() + 1;

        McardNotice notice = McardNotice.builder()
            .mcard(mcard).title(requestDto.getTitle()).content(requestDto.getContent())
            .imageUrl(requestDto.getImageUrl()).displayOrder(nextOrder).build();

        return McardNoticeResponseDto.from(mcardNoticeRepository.save(notice));
    }

    @Override
    @Transactional
    public McardNoticeResponseDto updateNotice(Long mcardId, Long noticeId, McardNoticeRequestDto requestDto) {
        McardNotice notice = mcardNoticeRepository.findById(noticeId)
            .orElseThrow(() -> new EntityNotFoundException("안내사항을 찾을 수 없습니다. noticeId=" + noticeId));
        if (!notice.getMcard().getMcardId().equals(mcardId))
            throw new IllegalArgumentException("해당 안내사항은 요청한 청첩장에 속하지 않습니다.");

        McardNotice updated = McardNotice.builder()
            .noticeId(notice.getNoticeId()).mcard(notice.getMcard())
            .title(requestDto.getTitle() != null ? requestDto.getTitle() : notice.getTitle())
            .content(requestDto.getContent() != null ? requestDto.getContent() : notice.getContent())
            .imageUrl(requestDto.getImageUrl() != null ? requestDto.getImageUrl() : notice.getImageUrl())
            .displayOrder(requestDto.getDisplayOrder() != null ? requestDto.getDisplayOrder() : notice.getDisplayOrder()).build();

        return McardNoticeResponseDto.from(mcardNoticeRepository.save(updated));
    }

    @Override
    @Transactional
    public void deleteNotice(Long mcardId, Long noticeId) {
        McardNotice notice = mcardNoticeRepository.findById(noticeId)
            .orElseThrow(() -> new EntityNotFoundException("안내사항을 찾을 수 없습니다. noticeId=" + noticeId));
        if (!notice.getMcard().getMcardId().equals(mcardId))
            throw new IllegalArgumentException("해당 안내사항은 요청한 청첩장에 속하지 않습니다.");
        mcardNoticeRepository.delete(notice);
    }
}
