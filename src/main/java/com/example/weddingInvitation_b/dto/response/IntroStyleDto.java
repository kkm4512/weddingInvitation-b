package com.example.weddingInvitation_b.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 인트로 레이아웃 스타일 정보 DTO
 *
 * <p>GET /intros 응답에서 선택 가능한 인트로 스타일 목록을 반환할 때 사용한다.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntroStyleDto {

    /** 스타일 식별 키 (McardIntro.introStyleKey에 저장되는 값) */
    private String styleKey;

    /** 스타일 표시 이름 */
    private String name;

    /** 스타일 설명 */
    private String description;
}
