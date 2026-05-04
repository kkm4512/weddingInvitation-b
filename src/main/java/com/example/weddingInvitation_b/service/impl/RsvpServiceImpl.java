package com.example.weddingInvitation_b.service.impl;

import com.example.weddingInvitation_b.domain.Mcard;
import com.example.weddingInvitation_b.domain.RsvpResponse;
import com.example.weddingInvitation_b.domain.RsvpSetting;
import com.example.weddingInvitation_b.dto.request.RsvpResponseRequestDto;
import com.example.weddingInvitation_b.dto.request.RsvpSettingRequestDto;
import com.example.weddingInvitation_b.dto.response.RsvpResponseResponseDto;
import com.example.weddingInvitation_b.dto.response.RsvpSettingResponseDto;
import com.example.weddingInvitation_b.exception.EntityNotFoundException;
import com.example.weddingInvitation_b.repository.McardRepository;
import com.example.weddingInvitation_b.repository.RsvpResponseRepository;
import com.example.weddingInvitation_b.repository.RsvpSettingRepository;
import com.example.weddingInvitation_b.service.RsvpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * RSVP 서비스 구현체
 *
 * @see RsvpService
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RsvpServiceImpl implements RsvpService {

    private final RsvpSettingRepository rsvpSettingRepository;
    private final RsvpResponseRepository rsvpResponseRepository;
    private final McardRepository mcardRepository;

    @Override
    public RsvpSettingResponseDto getSetting(Long mcardId) {
        RsvpSetting setting = rsvpSettingRepository.findByMcardMcardId(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("RSVP 설정을 찾을 수 없습니다. mcardId=" + mcardId));
        return RsvpSettingResponseDto.from(setting);
    }

    @Override
    @Transactional
    public RsvpSettingResponseDto saveSetting(Long mcardId, RsvpSettingRequestDto requestDto) {
        Mcard mcard = mcardRepository.findById(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("청첩장을 찾을 수 없습니다. mcardId=" + mcardId));

        RsvpSetting existing = rsvpSettingRepository.findByMcardMcardId(mcardId)
            .orElse(RsvpSetting.builder().mcard(mcard).build());

        RsvpSetting setting = RsvpSetting.builder()
            .rsvpSettingId(existing.getRsvpSettingId()).mcard(mcard)
            .isEnabled(requestDto.getIsEnabled() != null ? requestDto.getIsEnabled() : existing.getIsEnabled())
            .build();

        return RsvpSettingResponseDto.from(rsvpSettingRepository.save(setting));
    }

    /**
     * 하객 참석의사 응답 제출 (인증 불필요)
     *
     * @param mcardId    청첩장 ID
     * @param requestDto 참석의사 정보
     * @return 저장된 응답 정보
     * @throws EntityNotFoundException 청첩장이 없거나 RSVP가 비활성화된 경우
     */
    @Override
    @Transactional
    public RsvpResponseResponseDto submitResponse(Long mcardId, RsvpResponseRequestDto requestDto) {
        Mcard mcard = mcardRepository.findById(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("청첩장을 찾을 수 없습니다. mcardId=" + mcardId));

        // RSVP 활성 여부 확인
        rsvpSettingRepository.findByMcardMcardId(mcardId).ifPresent(setting -> {
            if (Boolean.FALSE.equals(setting.getIsEnabled())) {
                throw new IllegalStateException("RSVP 기능이 비활성화되어 있습니다. mcardId=" + mcardId);
            }
        });

        RsvpResponse response = RsvpResponse.builder()
            .mcard(mcard)
            .responderName(requestDto.getResponderName())
            .responderPhone(requestDto.getResponderPhone())
            .willAttend(requestDto.getWillAttend())
            .attendeeCount(requestDto.getAttendeeCount() != null ? requestDto.getAttendeeCount() : 1)
            .message(requestDto.getMessage())
            .build();

        return RsvpResponseResponseDto.from(rsvpResponseRepository.save(response));
    }

    @Override
    public List<RsvpResponseResponseDto> getResponses(Long mcardId) {
        return rsvpResponseRepository.findByMcardMcardIdOrderByRespondedAtDesc(mcardId)
            .stream().map(RsvpResponseResponseDto::from).collect(Collectors.toList());
    }
}
