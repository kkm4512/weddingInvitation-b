package com.example.weddingInvitation_b.service.impl;

import com.example.weddingInvitation_b.domain.Mcard;
import com.example.weddingInvitation_b.domain.McardContact;
import com.example.weddingInvitation_b.dto.request.McardContactRequestDto;
import com.example.weddingInvitation_b.dto.response.McardContactResponseDto;
import com.example.weddingInvitation_b.exception.EntityNotFoundException;
import com.example.weddingInvitation_b.repository.McardContactRepository;
import com.example.weddingInvitation_b.repository.McardRepository;
import com.example.weddingInvitation_b.service.McardContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 연락처 서비스 구현체
 *
 * <p>청첩장의 신랑·신부·혼주 연락처 정보를 조회하고 저장한다.
 * 저장 시 기존 연락처를 모두 덮어쓴다 (전체 교체 방식).</p>
 *
 * @see McardContactService
 * @see McardContactRepository
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class McardContactServiceImpl implements McardContactService {

    private final McardContactRepository mcardContactRepository;
    private final McardRepository mcardRepository;

    /**
     * 연락처 목록 조회
     *
     * @param mcardId 청첩장 ID
     * @return 연락처 목록
     */
    @Override
    public List<McardContactResponseDto> getContacts(Long mcardId) {
        return mcardContactRepository.findByMcardMcardId(mcardId)
            .stream().map(McardContactResponseDto::from).collect(Collectors.toList());
    }

    /**
     * 연락처 목록 저장 (전체 교체)
     *
     * <p>기존 연락처를 모두 삭제하고 새 목록으로 저장한다.</p>
     *
     * @param mcardId    청첩장 ID
     * @param requestDto 저장할 연락처 목록
     * @return 저장된 연락처 목록
     * @throws EntityNotFoundException 청첩장이 존재하지 않을 경우
     */
    @Override
    @Transactional
    public List<McardContactResponseDto> saveContacts(Long mcardId, McardContactRequestDto requestDto) {
        Mcard mcard = mcardRepository.findById(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("청첩장을 찾을 수 없습니다. mcardId=" + mcardId));

        // 1. 기존 연락처 전체 삭제
        List<McardContact> existing = mcardContactRepository.findByMcardMcardId(mcardId);
        mcardContactRepository.deleteAll(existing);

        // 2. 새 연락처 저장 (contacts null 또는 빈 리스트면 빈 결과 반환)
        List<McardContactRequestDto.ContactItemDto> items = requestDto.getContacts();
        if (items == null || items.isEmpty()) {
            return List.of();
        }

        List<McardContact> saved = items.stream()
            .map(item -> McardContact.builder()
                .mcard(mcard)
                .contactType(item.getContactType())
                .name(item.getName())
                .phoneNumber(item.getPhoneNumber())
                .isVisible(item.getIsVisible() != null ? item.getIsVisible() : true)
                .build())
            .map(mcardContactRepository::save)
            .collect(Collectors.toList());

        return saved.stream().map(McardContactResponseDto::from).collect(Collectors.toList());
    }
}
