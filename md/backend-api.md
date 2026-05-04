# 모바일 청첩장 서비스 - 백엔드 API 명세

> 기준: React(Frontend) ↔ Spring Boot(Backend) RESTful API  
> 인증: 카카오 OAuth 2.0 소셜 로그인 + 자체 JWT (Authorization: Bearer)  
> Base URL: `/api/v1`

---

## 1. 인증 (Authentication)

| 메서드 | 엔드포인트 | 설명 |
|--------|-----------|------|
| GET | `/auth/kakao` | 카카오 OAuth 로그인 시작 |
| GET | `/auth/kakao/callback` | 카카오 OAuth 콜백 처리 및 JWT 발급 ({ userId, accessToken } JSON 반환) |
| GET | `/auth/me` | 현재 로그인 사용자 정보 조회 |
| GET | `/auth/test-login/{userId}` | **[개발 전용]** userId로 바로 JWT 발급 (카카오 없이 테스트용) |

---

## 2. 청첩장 (MCard) CRUD

| 메서드 | 엔드포인트 | 설명 |
|--------|-----------|------|
| GET | `/mcards` | 내 청첩장 목록 조회 |
| POST | `/mcards` | 청첩장 신규 생성 |
| GET | `/mcards/{mcardId}` | 청첩장 단건 조회 (편집용) |
| PUT | `/mcards/{mcardId}` | 청첩장 전체 저장 (편집 내용 저장) |
| DELETE | `/mcards/{mcardId}` | 청첩장 삭제 |
| GET | `/mcards/{mcardId}/preview` | 청첩장 미리보기 데이터 조회 |

---

## 3. 청첩장 공개 뷰 (하객용)

| 메서드 | 엔드포인트 | 설명 |
|--------|-----------|------|
| GET | `/w/{inviteCode}` | 초대 코드로 공개 청첩장 조회 (하객 뷰) |

---

## 4. 테마 설정

| 메서드 | 엔드포인트 | 설명 |
|--------|-----------|------|
| GET | `/themes` | 사용 가능한 테마 목록 조회 (인증 불필요) |
| GET | `/themes/{themeId}` | 테마 상세 조회 (색상/폰트 옵션 포함, 인증 불필요) |
| GET | `/mcards/{mcardId}/theme` | 청첩장 테마 설정 조회 |
| PUT | `/mcards/{mcardId}/theme` | 청첩장 테마 설정 저장 |

---

## 5. 인트로 설정

| 메서드 | 엔드포인트 | 설명 |
|--------|-----------|------|
| GET | `/intros` | 인트로 레이아웃 스타일 목록 조회 (인증 불필요) |
| GET | `/mcards/{mcardId}/intro` | 청첩장 인트로 스타일 조회 |
| PUT | `/mcards/{mcardId}/intro` | 청첩장 인트로 스타일 저장 |

---

## 6. 신랑·신부 정보

| 메서드 | 엔드포인트 | 설명 |
|--------|-----------|------|
| GET | `/mcards/{mcardId}/couple` | 신랑·신부 정보 조회 |
| PUT | `/mcards/{mcardId}/couple` | 신랑·신부 정보 저장 |

---

## 7. 모시는 글 (청첩 인사말)

| 메서드 | 엔드포인트 | 설명 |
|--------|-----------|------|
| GET | `/mcards/{mcardId}/greeting` | 인사말 조회 |
| PUT | `/mcards/{mcardId}/greeting` | 인사말 저장 |
| GET | `/greetings/samples` | 인사말 샘플 문구 목록 조회 |

---

## 8. 예식 일시

| 메서드 | 엔드포인트 | 설명 |
|--------|-----------|------|
| GET | `/mcards/{mcardId}/schedule` | 예식 일시 조회 |
| PUT | `/mcards/{mcardId}/schedule` | 예식 일시 저장 |

---

## 9. 예식 장소

| 메서드 | 엔드포인트 | 설명 |
|--------|-----------|------|
| GET | `/mcards/{mcardId}/venue` | 예식 장소 조회 |
| PUT | `/mcards/{mcardId}/venue` | 예식 장소 저장 |
| POST | `/mcards/{mcardId}/venue/transports` | 교통수단 안내 추가 |
| PUT | `/mcards/{mcardId}/venue/transports/{transportId}` | 교통수단 안내 수정 |
| DELETE | `/mcards/{mcardId}/venue/transports/{transportId}` | 교통수단 안내 삭제 |

---

## 10. 갤러리

| 메서드 | 엔드포인트 | 설명 |
|--------|-----------|------|
| GET | `/mcards/{mcardId}/gallery` | 갤러리 사진 목록 조회 |
| POST | `/mcards/{mcardId}/gallery` | 사진 업로드 |
| PUT | `/mcards/{mcardId}/gallery/order` | 사진 순서 변경 |
| DELETE | `/mcards/{mcardId}/gallery/{photoId}` | 사진 삭제 |
| PUT | `/mcards/{mcardId}/gallery/layout` | 갤러리 레이아웃 설정 저장 |

---

## 11. 연락하기

| 메서드 | 엔드포인트 | 설명 |
|--------|-----------|------|
| GET | `/mcards/{mcardId}/contacts` | 연락처 정보 조회 |
| PUT | `/mcards/{mcardId}/contacts` | 연락처 정보 저장 |

---

## 12. 계좌번호

| 메서드 | 엔드포인트 | 설명 |
|--------|-----------|------|
| GET | `/mcards/{mcardId}/accounts` | 계좌 목록 조회 |
| POST | `/mcards/{mcardId}/accounts` | 계좌 추가 |
| PUT | `/mcards/{mcardId}/accounts/{accountId}` | 계좌 수정 |
| DELETE | `/mcards/{mcardId}/accounts/{accountId}` | 계좌 삭제 |

---

## 13. 동영상

| 메서드 | 엔드포인트 | 설명 |
|--------|-----------|------|
| GET | `/mcards/{mcardId}/video` | 동영상 정보 조회 |
| PUT | `/mcards/{mcardId}/video` | 동영상 URL 및 제목 저장 |

---

## 14. 배경음악

| 메서드 | 엔드포인트 | 설명 |
|--------|-----------|------|
| GET | `/mcards/{mcardId}/bgm` | 배경음악 설정 조회 |
| PUT | `/mcards/{mcardId}/bgm` | 배경음악 설정 저장 |
| POST | `/mcards/{mcardId}/bgm/upload` | 배경음악 파일 업로드 |
| GET | `/bgm/samples` | 샘플 음악 목록 조회 |

---

## 15. 안내사항

| 메서드 | 엔드포인트 | 설명 |
|--------|-----------|------|
| GET | `/mcards/{mcardId}/notices` | 안내사항 목록 조회 |
| POST | `/mcards/{mcardId}/notices` | 안내사항 추가 |
| PUT | `/mcards/{mcardId}/notices/{noticeId}` | 안내사항 수정 |
| DELETE | `/mcards/{mcardId}/notices/{noticeId}` | 안내사항 삭제 |
| GET | `/notices/samples` | 안내사항 샘플 문구 목록 조회 |

---

## 16. 참석의사 (RSVP)

| 메서드 | 엔드포인트 | 설명 |
|--------|-----------|------|
| GET | `/mcards/{mcardId}/rsvp/settings` | RSVP 설정 조회 |
| PUT | `/mcards/{mcardId}/rsvp/settings` | RSVP 활성/비활성 설정 저장 |
| POST | `/mcards/{mcardId}/rsvp` | 하객 참석의사 응답 제출 |
| GET | `/mcards/{mcardId}/rsvp` | RSVP 응답 목록 조회 (제작자용) |

---

## 17. 방명록

| 메서드 | 엔드포인트 | 설명 |
|--------|-----------|------|
| GET | `/mcards/{mcardId}/guestbook/settings` | 방명록 설정 조회 |
| PUT | `/mcards/{mcardId}/guestbook/settings` | 방명록 활성/비활성 설정 저장 |
| GET | `/mcards/{mcardId}/guestbook` | 방명록 메시지 목록 조회 |
| POST | `/mcards/{mcardId}/guestbook` | 하객 방명록 메시지 작성 |
| PUT | `/mcards/{mcardId}/guestbook/{messageId}/reply` | 방명록 메시지에 답글 작성 |
| DELETE | `/mcards/{mcardId}/guestbook/{messageId}` | 방명록 메시지 삭제 |

---

## 18. 화환 보내기

| 메서드 | 엔드포인트 | 설명 |
|--------|-----------|------|
| GET | `/mcards/{mcardId}/wreath` | 화환 URL 설정 조회 |
| PUT | `/mcards/{mcardId}/wreath` | 화환 URL 설정 저장 |

---

## 19. 글귀

| 메서드 | 엔드포인트 | 설명 |
|--------|-----------|------|
| GET | `/mcards/{mcardId}/quote` | 글귀 조회 |
| PUT | `/mcards/{mcardId}/quote` | 글귀 저장 |
| GET | `/quotes/samples` | 글귀 샘플 문구 목록 조회 |

---

## 20. 사진 & 글귀

| 메서드 | 엔드포인트 | 설명 |
|--------|-----------|------|
| GET | `/mcards/{mcardId}/photo-quote` | 사진+글귀 블록 조회 |
| PUT | `/mcards/{mcardId}/photo-quote` | 사진+글귀 블록 저장 |
| POST | `/mcards/{mcardId}/photo-quote/upload` | 사진+글귀 이미지 업로드 |

---

## 21. 공유 썸네일

| 메서드 | 엔드포인트 | 설명 |
|--------|-----------|------|
| GET | `/mcards/{mcardId}/thumbnail` | 공유 썸네일 설정 조회 |
| PUT | `/mcards/{mcardId}/thumbnail` | 공유 썸네일 설정 저장 |
| POST | `/mcards/{mcardId}/thumbnail/upload?type={type}` | 썸네일 이미지 업로드 |

> - `type` 파라미터: `kakao` (카카오톡 공유용) 또는 `url` (URL 공유용, 기본값)

---

## 22. 메뉴 순서

| 메서드 | 엔드포인트 | 설명 |
|--------|-----------|------|
| GET | `/mcards/{mcardId}/sections/order` | 섹션 노출 순서 조회 |
| PUT | `/mcards/{mcardId}/sections/order` | 섹션 노출 순서 저장 |

---

## 23. QR 코드

| 메서드 | 엔드포인트 | 설명 |
|--------|-----------|------|
| GET | `/mcards/{mcardId}/qrcode` | QR 코드 이미지 생성 및 반환 |

---

## 24. 파일 업로드 (공통)

| 메서드 | 엔드포인트 | 설명 |
|--------|-----------|------|
| POST | `/files/upload?folder={folder}` | 이미지/파일 업로드 (multipart/form-data, Cloudflare R2 저장 후 URL 반환) |
| DELETE | `/files?fileUrl={fileUrl}` | 업로드 파일 삭제 |

> - `folder` 파라미터: 저장 경로 구분용 (예: `gallery`, `bgm`, `photo-quote`, `thumbnail`, `test`)
> - 요청 필드: `file` (multipart/form-data)
> - 응답: `{ fileUrl: "https://..." }`

---

## 25. 헬스 체크

| 메서드 | 엔드포인트 | 설명 |
|--------|-----------|------|
| GET | `/health` | 서버 상태 확인 (인증 불필요, Docker/Nginx/CI-CD 공통 사용) |

**응답 예시 (정상)**
```json
{
  "success": true,
  "data": {
    "status": "UP",
    "timestamp": "2026-05-03T14:30:00",
    "service": "wedding-invitation-backend",
    "version": "1.0.0"
  }
}
```

---

## API 설계 참고사항

- 모든 인증 필요 API는 **JWT 기반**으로 처리한다
  - 카카오 OAuth 2.0 로그인 성공 시 서버가 자체 JWT를 생성하여 `{ userId, accessToken }` JSON으로 응답
  - 이후 모든 요청에서 클라이언트가 `Authorization: Bearer {accessToken}` 헤더를 포함하고, 서버의 JWT Filter가 서명 검증 후 SecurityContext에 userId 저장
  - JWT 스펙: HS256 알고리즘, payload에 `userId` 포함, 만료 7일
- 하객용 API (`/w/{inviteCode}`, RSVP 제출, 방명록 작성 등)는 **인증 불필요 (permitAll)**
- 정적 목록 API (`/intros`, `/themes`, `/themes/**`, `/greetings/samples`, `/quotes/samples`, `/notices/samples`, `/bgm/samples`)는 **인증 불필요 (permitAll)**
- 파일 업로드는 **Cloudflare R2 (S3 호환)** 에 저장되며, 환경변수로 설정한다
- QR 코드 API (`/mcards/{mcardId}/qrcode`)는 `image/png` 바이너리를 직접 반환한다 (Content-Disposition: attachment)
- 모든 JSON API 응답은 공통 래퍼로 감싼다: `{ code, message, datas }`

### 공통 응답 형식

```json
{
  "code": "SUCCESS",
  "message": "요청이 성공적으로 처리되었습니다.",
  "datas": { ... }
}
```

### 환경 변수 목록

| 변수명 | 설명 |
|--------|------|
| `KAKAO_CLIENT_ID` | 카카오 OAuth 앱 키 |
| `KAKAO_CLIENT_SECRET` | 카카오 OAuth 시크릿 |
| `KAKAO_REDIRECT_URI` | 카카오 콜백 URL |
| `CLOUDFLARE_ACCOUNT_ID` | R2 계정 ID |
| `CLOUDFLARE_ACCESS_KEY_ID` | R2 액세스 키 |
| `CLOUDFLARE_SECRET_ACCESS_KEY` | R2 시크릿 키 |
| `CLOUDFLARE_BUCKET_NAME` | R2 버킷명 |
| `CLOUDFLARE_BUCKET_URL` | R2 퍼블릭 URL (https://...) |
| `JWT_SECRET` | JWT 서명 비밀키 |
| `APP_BASE_URL` | 서비스 기본 URL — QR 코드 생성 시 사용 (예: `https://mcard.example.com`) |
| `SPRING_PROFILES_ACTIVE` | 활성 프로필 (`local` / `dev` / `prd`) |
