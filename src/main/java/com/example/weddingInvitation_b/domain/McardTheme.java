package com.example.weddingInvitation_b.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 청첩장 테마 설정 엔티티
 * 
 * <p>각 청첩장의 시각적 테마, 색상, 글꼴 등을 관리한다.
 * 사용자가 선택한 테마 스타일을 저장하고, 실시간 미리보기에 적용된다.</p>
 */
@Entity
@Table(name = "mcard_themes")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class McardTheme {
    
    /** 테마 설정 고유식별자 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long themeId;
    
    /** 청첩장 */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mcard_id", nullable = false, unique = true)
    private Mcard mcard;
    
    /** 테마 스타일 (예: momentum, classic, minimal) */
    private String themeStyle;
    
    /** 색상 (예: white, beige, pink) */
    private String color;
    
    /** 글꼴 (예: 고운고딕, 나눔명조) */
    private String fontFamily;
    
    /** 글자 굵기 (예: normal, bold) */
    private String fontWeight;
    
    /** 확대 방지 여부 */
    @Builder.Default
    private Boolean preventZoom = false;
    
    /** 스크롤 등장 효과 여부 */
    @Builder.Default
    private Boolean enableScrollAnimation = true;
}

