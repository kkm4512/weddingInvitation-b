package com.example.weddingInvitation_b.service.impl;

import com.example.weddingInvitation_b.domain.GuestbookMessage;
import com.example.weddingInvitation_b.domain.GuestbookSetting;
import com.example.weddingInvitation_b.domain.Mcard;
import com.example.weddingInvitation_b.dto.request.GuestbookMessageRequestDto;
import com.example.weddingInvitation_b.dto.request.GuestbookSettingRequestDto;
import com.example.weddingInvitation_b.dto.response.GuestbookMessageResponseDto;
import com.example.weddingInvitation_b.dto.response.GuestbookSettingResponseDto;
import com.example.weddingInvitation_b.exception.EntityNotFoundException;
import com.example.weddingInvitation_b.repository.GuestbookMessageRepository;
import com.example.weddingInvitation_b.repository.GuestbookSettingRepository;
import com.example.weddingInvitation_b.repository.McardRepository;
import com.example.weddingInvitation_b.service.GuestbookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 방명록 서비스 구현체
 *
 * @see GuestbookService
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GuestbookServiceImpl implements GuestbookService {

    private final GuestbookSettingRepository guestbookSettingRepository;
    private final GuestbookMessageRepository guestbookMessageRepository;
    private final McardRepository mcardRepository;

    @Override
    public GuestbookSettingResponseDto getSetting(Long mcardId) {
        GuestbookSetting setting = guestbookSettingRepository.findByMcardMcardId(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("방명록 설정을 찾을 수 없습니다. mcardId=" + mcardId));
        return GuestbookSettingResponseDto.from(setting);
    }

    @Override
    @Transactional
    public GuestbookSettingResponseDto saveSetting(Long mcardId, GuestbookSettingRequestDto requestDto) {
        Mcard mcard = mcardRepository.findById(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("청첩장을 찾을 수 없습니다. mcardId=" + mcardId));

        GuestbookSetting existing = guestbookSettingRepository.findByMcardMcardId(mcardId)
            .orElse(GuestbookSetting.builder().mcard(mcard).build());

        GuestbookSetting setting = GuestbookSetting.builder()
            .guestbookSettingId(existing.getGuestbookSettingId()).mcard(mcard)
            .isEnabled(requestDto.getIsEnabled() != null ? requestDto.getIsEnabled() : existing.getIsEnabled())
            .build();

        return GuestbookSettingResponseDto.from(guestbookSettingRepository.save(setting));
    }

    @Override
    public List<GuestbookMessageResponseDto> getMessages(Long mcardId) {
        return guestbookMessageRepository.findByMcardMcardIdAndIsDeletedFalseOrderByCreatedAtDesc(mcardId)
            .stream().map(GuestbookMessageResponseDto::from).collect(Collectors.toList());
    }

    /**
     * 하객 방명록 메시지 작성
     *
     * @param mcardId    청첩장 ID
     * @param requestDto 메시지 내용
     * @return 저장된 메시지
     * @throws IllegalStateException 방명록이 비활성화된 경우
     */
    @Override
    @Transactional
    public GuestbookMessageResponseDto addMessage(Long mcardId, GuestbookMessageRequestDto requestDto) {
        Mcard mcard = mcardRepository.findById(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("청첩장을 찾을 수 없습니다. mcardId=" + mcardId));

        guestbookSettingRepository.findByMcardMcardId(mcardId).ifPresent(setting -> {
            if (Boolean.FALSE.equals(setting.getIsEnabled())) {
                throw new IllegalStateException("방명록 기능이 비활성화되어 있습니다. mcardId=" + mcardId);
            }
        });

        Boolean isSecret = requestDto.getIsSecret();
        if (isSecret == null) {
            isSecret = requestDto.getPassword() != null && !requestDto.getPassword().isBlank();
        }

        GuestbookMessage message = GuestbookMessage.builder()
            .mcard(mcard).guestName(requestDto.getGuestName()).content(requestDto.getContent())
            .isSecret(Boolean.TRUE.equals(isSecret))
            .build();

        return GuestbookMessageResponseDto.from(guestbookMessageRepository.save(message));
    }

    /**
     * 방명록 메시지 삭제 (soft delete)
     *
     * @param mcardId   청첩장 ID
     * @param messageId 삭제할 메시지 ID
     */
    @Override
    @Transactional
    public void deleteMessage(Long mcardId, Long messageId) {
        GuestbookMessage message = guestbookMessageRepository.findById(messageId)
            .orElseThrow(() -> new EntityNotFoundException("방명록 메시지를 찾을 수 없습니다. messageId=" + messageId));

        if (!message.getMcard().getMcardId().equals(mcardId))
            throw new IllegalArgumentException("해당 메시지는 요청한 청첩장에 속하지 않습니다.");

        GuestbookMessage deleted = GuestbookMessage.builder()
            .messageId(message.getMessageId()).mcard(message.getMcard())
            .guestName(message.getGuestName()).content(message.getContent())
            .isSecret(message.getIsSecret()).isDeleted(true).build();

        guestbookMessageRepository.save(deleted);
    }
}
