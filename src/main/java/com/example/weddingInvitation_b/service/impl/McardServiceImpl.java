package com.example.weddingInvitation_b.service.impl;

import com.example.weddingInvitation_b.domain.Mcard;
import com.example.weddingInvitation_b.domain.User;
import com.example.weddingInvitation_b.dto.request.McardCreateRequestDto;
import com.example.weddingInvitation_b.dto.response.McardResponseDto;
import com.example.weddingInvitation_b.exception.AccessDeniedException;
import com.example.weddingInvitation_b.exception.EntityNotFoundException;
import com.example.weddingInvitation_b.repository.McardRepository;
import com.example.weddingInvitation_b.repository.UserRepository;
import com.example.weddingInvitation_b.service.McardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 청첩장 서비스 구현체
 * 
 * <p>청첩장의 CRUD 및 기본 비즈니스 로직을 처리한다.
 * 초대 코드는 자동으로 생성되며, 권한 검증을 통해 사용자의 청첩장만 조회/수정할 수 있다.</p>
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class McardServiceImpl implements McardService {

    private final McardRepository mcardRepository;
    private final UserRepository userRepository;

    /**
     * 사용자의 청첩장 목록 조회
     * 
     * @param userId 사용자 ID
     * @return 사용자의 청첩장 목록
     */
    @Override
    public List<McardResponseDto> getMyMcards(Long userId) {
        // 사용자 존재 확인
        userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. userId=" + userId));

        // 사용자의 청첩장 목록 조회
        List<Mcard> mcards = mcardRepository.findByUserUserIdAndIsDeletedFalse(userId);
        return mcards.stream()
            .map(McardResponseDto::from)
            .collect(Collectors.toList());
    }

    /**
     * 청첩장 단건 조회 (편집용)
     * 
     * @param mcardId 청첩장 ID
     * @return 청첩장 정보
     * @throws EntityNotFoundException 청첩장이 존재하지 않을 경우
     */
    @Override
    public McardResponseDto getMcard(Long mcardId) {
        Mcard mcard = mcardRepository.findByMcardIdAndIsDeletedFalse(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("청첩장을 찾을 수 없습니다. mcardId=" + mcardId));
        return McardResponseDto.from(mcard);
    }

    /**
     * 청첩장 공개 조회 (초대 코드로)
     * 
     * @param inviteCode 초대 코드
     * @return 청첩장 정보 (인증 불필요)
     * @throws EntityNotFoundException 초대 코드에 해당하는 청첩장이 없을 경우
     */
    @Override
    public McardResponseDto getMcardByInviteCode(String inviteCode) {
        Mcard mcard = mcardRepository.findByInviteCode(inviteCode)
            .orElseThrow(() -> new EntityNotFoundException("청첩장을 찾을 수 없습니다. inviteCode=" + inviteCode));
        return McardResponseDto.from(mcard);
    }

    /**
     * 청첩장 신규 생성
     * 
     * <p>새로운 청첩장을 생성하고 고유한 초대 코드를 자동으로 부여한다.</p>
     * 
     * @param requestDto 생성 요청 정보 (제목)
     * @param userId 청첩장 소유자 ID
     * @return 생성된 청첩장 정보
     * @throws EntityNotFoundException 사용자가 존재하지 않을 경우
     */
    @Override
    @Transactional
    public McardResponseDto createMcard(McardCreateRequestDto requestDto, Long userId) {
        // 사용자 존재 확인
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. userId=" + userId));

        // 청첩장 생성 - 초대 코드는 UUID의 처음 8자리로 생성
        String inviteCode = UUID.randomUUID().toString().substring(0, 8);
        Mcard mcard = Mcard.builder()
            .user(user)
            .title(requestDto.getTitle())
            .inviteCode(inviteCode)
            .hasWatermark(true)  // 기본값: 워터마크 표시
            .isDeleted(false)
            .build();

        Mcard savedMcard = mcardRepository.save(mcard);
        return McardResponseDto.from(savedMcard);
    }

    /**
     * 청첩장 정보 업데이트
     * 
     * @param mcardId 업데이트할 청첩장 ID
     * @param requestDto 업데이트 정보
     * @param userId 요청한 사용자 ID
     * @return 업데이트된 청첩장 정보
     * @throws EntityNotFoundException 청첩장이 존재하지 않을 경우
     * @throws AccessDeniedException 청첩장 소유자가 아닐 경우
     */
    @Override
    @Transactional
    public McardResponseDto updateMcard(Long mcardId, McardCreateRequestDto requestDto, Long userId) {
        Mcard mcard = mcardRepository.findById(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("청첩장을 찾을 수 없습니다. mcardId=" + mcardId));

        // 권한 검증: 청첩장 소유자만 수정 가능
        if (!mcard.getUser().getUserId().equals(userId)) {
            throw new AccessDeniedException("청첩장을 수정할 권한이 없습니다.");
        }

        // 제목 업데이트 (필요시 다른 필드도 추가 가능)
        mcard = Mcard.builder()
            .mcardId(mcard.getMcardId())
            .user(mcard.getUser())
            .title(requestDto.getTitle() != null ? requestDto.getTitle() : mcard.getTitle())
            .inviteCode(mcard.getInviteCode())
            .hasWatermark(mcard.getHasWatermark())
            .weddingDateTime(mcard.getWeddingDateTime())
            .createdAt(mcard.getCreatedAt())
            .updatedAt(mcard.getUpdatedAt())
            .isDeleted(mcard.getIsDeleted())
            .build();

        Mcard updatedMcard = mcardRepository.save(mcard);
        return McardResponseDto.from(updatedMcard);
    }

    /**
     * 청첩장 삭제 (soft delete)
     * 
     * @param mcardId 삭제할 청첩장 ID
     * @param userId 요청한 사용자 ID
     * @throws EntityNotFoundException 청첩장이 존재하지 않을 경우
     * @throws AccessDeniedException 청첩장 소유자가 아닐 경우
     */
    @Override
    @Transactional
    public void deleteMcard(Long mcardId, Long userId) {
        Mcard mcard = mcardRepository.findById(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("청첩장을 찾을 수 없습니다. mcardId=" + mcardId));

        // 권한 검증: 청첩장 소유자만 삭제 가능
        if (!mcard.getUser().getUserId().equals(userId)) {
            throw new AccessDeniedException("청첩장을 삭제할 권한이 없습니다.");
        }

        // soft delete - isDeleted 플래그 설정
        mcard = Mcard.builder()
            .mcardId(mcard.getMcardId())
            .user(mcard.getUser())
            .title(mcard.getTitle())
            .inviteCode(mcard.getInviteCode())
            .hasWatermark(mcard.getHasWatermark())
            .weddingDateTime(mcard.getWeddingDateTime())
            .createdAt(mcard.getCreatedAt())
            .updatedAt(mcard.getUpdatedAt())
            .isDeleted(true)
            .build();

        mcardRepository.save(mcard);
    }
}
