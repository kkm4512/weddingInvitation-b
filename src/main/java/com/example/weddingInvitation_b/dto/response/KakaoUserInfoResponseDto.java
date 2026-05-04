package com.example.weddingInvitation_b.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 카카오 사용자 정보 응답 DTO
 *
 * <p>카카오 사용자 정보 엔드포인트(https://kapi.kakao.com/v2/user/me)에서
 * 반환되는 사용자 프로필 정보를 담는다.</p>
 *
 * <p>카카오 응답 구조:
 * <pre>
 * {
 *   "id": 1234567890,
 *   "kakao_account": {
 *     "email": "user@kakao.com",
 *     "profile": {
 *       "nickname": "홍길동",
 *       "profile_image_url": "https://..."
 *     }
 *   }
 * }
 * </pre>
 * </p>
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class KakaoUserInfoResponseDto {

    /** 카카오 사용자 고유 ID */
    private Long id;

    /** 카카오 계정 정보 */
    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    /**
     * 이메일 반환 편의 메서드
     *
     * @return 카카오 계정 이메일 (없으면 null)
     */
    public String getEmail() {
        return kakaoAccount != null ? kakaoAccount.getEmail() : null;
    }

    /**
     * 닉네임 반환 편의 메서드
     *
     * @return 카카오 프로필 닉네임 (없으면 null)
     */
    public String getNickname() {
        if (kakaoAccount == null || kakaoAccount.getProfile() == null) return null;
        return kakaoAccount.getProfile().getNickname();
    }

    /**
     * 프로필 이미지 URL 반환 편의 메서드
     *
     * @return 카카오 프로필 이미지 URL (없으면 null)
     */
    public String getProfileImageUrl() {
        if (kakaoAccount == null || kakaoAccount.getProfile() == null) return null;
        return kakaoAccount.getProfile().getProfileImageUrl();
    }

    // ──────────────────────────────────────────────
    // 중첩 클래스
    // ──────────────────────────────────────────────

    /**
     * 카카오 계정 정보
     */
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KakaoAccount {

        /** 카카오 계정 이메일 */
        private String email;

        /** 카카오 프로필 정보 */
        private Profile profile;
    }

    /**
     * 카카오 프로필 정보
     */
    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Profile {

        /** 카카오 닉네임 */
        private String nickname;

        /** 카카오 프로필 이미지 URL */
        @JsonProperty("profile_image_url")
        private String profileImageUrl;
    }
}
