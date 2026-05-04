package com.example.weddingInvitation_b.service.impl;

import com.example.weddingInvitation_b.domain.Mcard;
import com.example.weddingInvitation_b.domain.McardSectionOrder;
import com.example.weddingInvitation_b.dto.request.McardSectionOrderRequestDto;
import com.example.weddingInvitation_b.dto.response.McardSectionOrderResponseDto;
import com.example.weddingInvitation_b.exception.EntityNotFoundException;
import com.example.weddingInvitation_b.repository.McardRepository;
import com.example.weddingInvitation_b.repository.McardSectionOrderRepository;
import com.example.weddingInvitation_b.service.McardSectionOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 메뉴 순서 서비스 구현체
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class McardSectionOrderServiceImpl implements McardSectionOrderService {

    private final McardSectionOrderRepository mcardSectionOrderRepository;
    private final McardRepository mcardRepository;

    @Override
    public McardSectionOrderResponseDto getSectionOrder(Long mcardId) {
        McardSectionOrder sectionOrder = mcardSectionOrderRepository.findByMcardMcardId(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("섹션 순서 정보를 찾을 수 없습니다. mcardId=" + mcardId));
        return McardSectionOrderResponseDto.from(sectionOrder);
    }

    @Override
    @Transactional
    public McardSectionOrderResponseDto saveSectionOrder(Long mcardId, McardSectionOrderRequestDto requestDto) {
        Mcard mcard = mcardRepository.findById(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("청첩장을 찾을 수 없습니다. mcardId=" + mcardId));

        McardSectionOrder existing = mcardSectionOrderRepository.findByMcardMcardId(mcardId)
            .orElse(McardSectionOrder.builder().mcard(mcard).build());

        String jsonOrder = existing.getSectionOrder();
        if (requestDto.getSectionOrder() != null) {
            jsonOrder = toJsonArray(requestDto.getSectionOrder());
        }

        McardSectionOrder sectionOrder = McardSectionOrder.builder()
            .sectionOrderId(existing.getSectionOrderId())
            .mcard(mcard)
            .sectionOrder(jsonOrder)
            .build();

        return McardSectionOrderResponseDto.from(mcardSectionOrderRepository.save(sectionOrder));
    }

    /**
     * List<String> → JSON 배열 문자열 (Jackson 없이 순수 Java)
     * 예: ["greeting","video"] → "[\"greeting\",\"video\"]"
     */
    private static String toJsonArray(List<String> list) {
        if (list == null || list.isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append("\"").append(list.get(i).replace("\"", "\\\"")).append("\"");
            if (i < list.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }
}
