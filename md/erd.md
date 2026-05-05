# Entity Relationship Diagram

> 기준: `com.example.weddingInvitation_b.domain` 패키지 전체 Entity 클래스  
> 작성일: 2026-05-05

---

## ERD (Mermaid)

```mermaid
erDiagram

    %% ── 사용자 ──────────────────────────────────────────────
    users {
        BIGINT      user_id         PK  "AUTO_INCREMENT"
        VARCHAR     provider            "kakao / google / naver"
        VARCHAR     provider_id     UK  "소셜 고유 ID"
        VARCHAR     name
        VARCHAR     email           UK
        VARCHAR     profile_image_url
        DATETIME    created_at
        DATETIME    updated_at
        BOOLEAN     is_deleted          "soft delete"
    }

    %% ── 청첩장 (핵심) ───────────────────────────────────────
    mcards {
        BIGINT      mcard_id        PK  "AUTO_INCREMENT"
        BIGINT      user_id         FK
        VARCHAR     title
        VARCHAR(20) invite_code     UK  "공개 URL 접근 코드"
        BOOLEAN     has_watermark       "default true"
        DATETIME    wedding_date_time
        DATETIME    created_at
        DATETIME    updated_at
        BOOLEAN     is_deleted          "soft delete"
    }

    %% ── 1:1 섹션 테이블 ─────────────────────────────────────
    mcard_themes {
        BIGINT      theme_id        PK
        BIGINT      mcard_id        FK  UK
        VARCHAR     theme_style
        VARCHAR     color
        VARCHAR     font_family
        VARCHAR     font_weight
        BOOLEAN     prevent_zoom        "default false"
        BOOLEAN     enable_scroll_animation "default true"
    }

    mcard_intros {
        BIGINT      intro_id        PK
        BIGINT      mcard_id        FK  UK
        VARCHAR     intro_style_key     "DEFAULT/EDGE/OVAL/FILL/ARGE 등"
        DATETIME    created_at
        DATETIME    updated_at
    }

    mcard_couples {
        BIGINT      couple_id       PK
        BIGINT      mcard_id        FK  UK
        VARCHAR     groom_name
        VARCHAR     bride_name
        VARCHAR     groom_father_name
        VARCHAR     groom_mother_name
        VARCHAR     bride_father_name
        VARCHAR     bride_mother_name
        BOOLEAN     groom_father_deceased
        BOOLEAN     groom_mother_deceased
        BOOLEAN     bride_father_deceased
        BOOLEAN     bride_mother_deceased
        BOOLEAN     show_groom_contacts "default true"
        BOOLEAN     show_bride_contacts "default true"
        DATETIME    created_at
        DATETIME    updated_at
    }

    mcard_greetings {
        BIGINT      greeting_id     PK
        BIGINT      mcard_id        FK  UK
        VARCHAR     title
        LONGTEXT    content
        VARCHAR     image_url
        VARCHAR     font_size           "small/medium/large, default medium"
    }

    mcard_schedules {
        BIGINT      schedule_id     PK
        BIGINT      mcard_id        FK  UK
        DATETIME    wedding_date_time
        INT         prep_time_minutes   "default 30"
        DATETIME    created_at
        DATETIME    updated_at
    }

    mcard_venues {
        BIGINT      venue_id        PK
        BIGINT      mcard_id        FK  UK
        VARCHAR     venue_name
        VARCHAR     floor_info          "층/홀 정보"
        VARCHAR     address
        DOUBLE      latitude
        DOUBLE      longitude
        VARCHAR     map_image_url       "네이버 Static Map 자동 생성 URL"
        BOOLEAN     show_map            "default true"
        BOOLEAN     map_locked          "default false"
        BOOLEAN     show_transport_icons "default true"
    }

    mcard_bgms {
        BIGINT      bgm_id          PK
        BIGINT      mcard_id        FK  UK
        VARCHAR     bgm_url
        VARCHAR     public_url
        VARCHAR     bgm_title
        BOOLEAN     auto_play           "default false"
    }

    mcard_quotes {
        BIGINT      quote_id        PK
        BIGINT      mcard_id        FK  UK
        TEXT        quote_content
        VARCHAR     font_size           "default medium"
    }

    mcard_photo_quotes {
        BIGINT      photo_quote_id  PK
        BIGINT      mcard_id        FK  UK
        VARCHAR     image_url
        TEXT        quote_text
    }

    mcard_videos {
        BIGINT      video_id        PK
        BIGINT      mcard_id        FK  UK
        VARCHAR     video_url
        VARCHAR     video_title
    }

    mcard_wreaths {
        BIGINT      wreath_id       PK
        BIGINT      mcard_id        FK  UK
        VARCHAR     wreath_url
    }

    mcard_thumbnails {
        BIGINT      thumbnail_id        PK
        BIGINT      mcard_id            FK  UK
        VARCHAR     kakaotalk_thumbnail_url
        VARCHAR     kakaotalk_public_url
        VARCHAR     url_share_thumbnail_url
        VARCHAR     url_share_public_url
    }

    mcard_section_orders {
        BIGINT      section_order_id PK
        BIGINT      mcard_id         FK  UK
        JSON        section_order       "섹션 순서 배열 JSON"
    }

    %% ── 1:N 섹션 테이블 ─────────────────────────────────────
    venue_transports {
        BIGINT      transport_id    PK
        BIGINT      venue_id        FK
        VARCHAR     transport_type      "subway/bus/car"
        TEXT        description
        INT         display_order       "default 0"
    }

    gallery_photos {
        BIGINT      photo_id        PK
        BIGINT      mcard_id        FK
        VARCHAR     image_url
        VARCHAR     public_url
        INT         display_order
        VARCHAR     layout_type         "default grid"
    }

    mcard_contacts {
        BIGINT      contact_id      PK
        BIGINT      mcard_id        FK
        VARCHAR     contact_type        "groom/bride/groom_father/groom_mother/bride_father/bride_mother"
        VARCHAR     name
        VARCHAR     phone_number
        BOOLEAN     is_visible          "default true"
    }

    bank_accounts {
        BIGINT      account_id      PK
        BIGINT      mcard_id        FK
        VARCHAR     account_type        "groom/bride (JSON: side)"
        VARCHAR     bank_name
        VARCHAR     account_number
        VARCHAR     account_holder
        INT         display_order       "default 0"
    }

    mcard_notices {
        BIGINT      notice_id       PK
        BIGINT      mcard_id        FK
        VARCHAR     title
        TEXT        content
        VARCHAR     image_url
        INT         display_order       "default 0"
    }

    %% ── 하객 인터랙션 ────────────────────────────────────────
    rsvp_settings {
        BIGINT      rsvp_setting_id PK
        BIGINT      mcard_id        FK  UK
        BOOLEAN     is_enabled          "default true"
    }

    rsvp_responses {
        BIGINT      response_id     PK
        BIGINT      mcard_id        FK
        VARCHAR     responder_name
        VARCHAR     responder_phone
        BOOLEAN     will_attend
        INT         attendee_count      "default 1"
        TEXT        message
        DATETIME    responded_at
        DATETIME    updated_at
    }

    guestbook_settings {
        BIGINT      guestbook_setting_id PK
        BIGINT      mcard_id             FK  UK
        BOOLEAN     is_enabled               "default true"
    }

    guestbook_messages {
        BIGINT      message_id      PK
        BIGINT      mcard_id        FK
        VARCHAR     guest_name
        TEXT        content
        BOOLEAN     is_secret           "default false"
        DATETIME    created_at
        BOOLEAN     is_deleted          "soft delete"
    }

    %% ── 관계 정의 ────────────────────────────────────────────

    users                ||--o{ mcards               : "소유 (1:N)"

    mcards               ||--o| mcard_themes          : "1:1"
    mcards               ||--o| mcard_intros          : "1:1"
    mcards               ||--o| mcard_couples         : "1:1"
    mcards               ||--o| mcard_greetings       : "1:1"
    mcards               ||--o| mcard_schedules       : "1:1"
    mcards               ||--o| mcard_venues          : "1:1"
    mcards               ||--o| mcard_bgms            : "1:1"
    mcards               ||--o| mcard_quotes          : "1:1"
    mcards               ||--o| mcard_photo_quotes    : "1:1"
    mcards               ||--o| mcard_videos          : "1:1"
    mcards               ||--o| mcard_wreaths         : "1:1"
    mcards               ||--o| mcard_thumbnails      : "1:1"
    mcards               ||--o| mcard_section_orders  : "1:1"
    mcards               ||--o| rsvp_settings         : "1:1"
    mcards               ||--o| guestbook_settings    : "1:1"

    mcards               ||--o{ gallery_photos        : "1:N"
    mcards               ||--o{ mcard_contacts        : "1:N"
    mcards               ||--o{ bank_accounts         : "1:N"
    mcards               ||--o{ mcard_notices         : "1:N"
    mcards               ||--o{ rsvp_responses        : "1:N"
    mcards               ||--o{ guestbook_messages    : "1:N"

    mcard_venues         ||--o{ venue_transports      : "1:N"
```

---

## 테이블 분류 요약

### 핵심 테이블
| 테이블 | 설명 |
|--------|------|
| `users` | 소셜 로그인 사용자 |
| `mcards` | 청첩장 루트 엔티티 |

### 1:1 섹션 테이블 (mcards 기준)
| 테이블 | 섹션 |
|--------|------|
| `mcard_themes` | 테마 설정 |
| `mcard_intros` | 인트로 스타일 |
| `mcard_couples` | 신랑·신부 정보 |
| `mcard_greetings` | 모시는 글 |
| `mcard_schedules` | 예식 일시 |
| `mcard_venues` | 예식 장소 |
| `mcard_bgms` | 배경음악 |
| `mcard_quotes` | 글귀 |
| `mcard_photo_quotes` | 사진 & 글귀 |
| `mcard_videos` | 동영상 |
| `mcard_wreaths` | 화환 보내기 |
| `mcard_thumbnails` | 공유 썸네일 |
| `mcard_section_orders` | 섹션 순서 |
| `rsvp_settings` | RSVP 활성 설정 |
| `guestbook_settings` | 방명록 활성 설정 |

### 1:N 테이블 (mcards 기준)
| 테이블 | 설명 |
|--------|------|
| `gallery_photos` | 갤러리 사진 목록 |
| `mcard_contacts` | 연락처 목록 |
| `bank_accounts` | 계좌번호 목록 |
| `mcard_notices` | 안내사항 목록 |
| `rsvp_responses` | 하객 참석 응답 |
| `guestbook_messages` | 하객 방명록 메시지 |

### 1:N 테이블 (mcard_venues 기준)
| 테이블 | 설명 |
|--------|------|
| `venue_transports` | 교통수단 안내 목록 |

---

## 설계 특이사항

- **Soft Delete**: `users`, `mcards`, `guestbook_messages`는 `is_deleted` 컬럼으로 논리 삭제 처리
- **1:1 섹션 분리**: 청첩장의 각 섹션(테마, 인트로, 커플 등)을 별도 테이블로 분리하여 SRP 준수 및 독립적인 저장/조회 가능
- **map_image_url**: `mcard_venues`에 저장되며, venue PUT 저장 시 서버가 lat/lng로 네이버 Static Map API를 호출해 자동 생성 후 Cloudflare R2 URL로 저장
- **section_order**: `mcard_section_orders.section_order`는 JSON 배열 컬럼으로 저장 (예: `["greeting","gallery","schedule"]`)
- **accountType → side**: `bank_accounts.account_type` 필드는 API 응답 시 `@JsonProperty("side")`로 직렬화됨
- **gallery layoutType**: 현재 `gallery_photos` 테이블에 `layout_type` 컬럼이 있으나 실제 갤러리 레이아웃 설정은 별도 `PUT /gallery/layout` API로 처리
