# 모바일 청첩장 서비스 - 백엔드 API 목록

> 기준: React(Frontend) ↔ Spring Boot(Backend) RESTful API  
> 인증: 카카오 OAuth 2.0 소셜 로그인 + 자체 JWT (`Authorization: Bearer {accessToken}`)  
> Base URL: `/api/v1`  
> 요청/응답 예시는 `api-spec.md` 참고

---

## 인증 정책

| 구분 | 설명 |
|------|------|
| JWT 필요 | 대부분의 편집/조회 API |
| 인증 불필요 (permitAll) | 하객 공개 뷰 (`/w/{inviteCode}`), RSVP 제출, 방명록 작성/조회 |
| 인증 불필요 (정적 목록) | `/intros`, `/themes`, `/themes/**`, `/greetings/samples`, `/quotes/samples`, `/notices/samples`, `/bgm/samples` |

JWT 스펙: HS256 알고리즘, payload에 `userId` 포함, 만료 7일. 카카오 OAuth 2.0 로그인 성공 시 서버가 자체 JWT를 생성하여 `{ userId, accessToken }` JSON으로 응답.

---

## 1. 인증 (Authentication)

| 메서드 | 엔드포인트 | 인증 | 설명 |
|--------|-----------|------|------|
| GET | `/auth/kakao` | 불필요 | 카카오 OAuth 로그인 시작 — 카카오 인증 URL 반환 |
| GET | `/auth/kakao/callback` | 불필요 | 카카오 OAuth 콜백 처리 및 자체 JWT 발급 |
| GET | `/auth/me` | 필요 | 현재 로그인 사용자 정보 조회 |
| GET | `/auth/test-login/{userId}` | 불필요 | **[개발 전용]** userId로 바로 JWT 발급 (카카오 없이 테스트용) |

---

## 2. 청첩장 (MCard) CRUD

| 메서드 | 엔드포인트 | 인증 | 설명 |
|--------|-----------|------|------|
| GET | `/mcards` | 필요 | 내 청첩장 목록 조회 |
| POST | `/mcards` | 필요 | 청첩장 신규 생성 |
| GET | `/mcards/{mcardId}` | 필요 | 청첩장 단건 조회 (편집용) |
| PUT | `/mcards/{mcardId}` | 필요 | 청첩장 전체 저장 |
| DELETE | `/mcards/{mcardId}` | 필요 | 청첩장 삭제 (soft delete) |
| GET | `/mcards/{mcardId}/preview` | 필요 | 청첩장 미리보기 데이터 조회 |

---

## 3. 청첩장 공개 뷰 (하객용)

| 메서드 | 엔드포인트 | 인증 | 설명 |
|--------|-----------|------|------|
| GET | `/w/{inviteCode}` | 불필요 | 초대 코드로 공개 청첩장 전체 데이터 조회 (하객 뷰) |

couple, greeting, schedule, venue, gallery, quote, video, accounts, contacts, rsvpSettings, guestbookSettings, guestbookMessages, wreath 등 하객 뷰에 필요한 모든 섹션 데이터를 한 번에 반환한다.

---

## 4. 테마 설정

| 메서드 | 엔드포인트 | 인증 | 설명 |
|--------|-----------|------|------|
| GET | `/themes` | 불필요 | 사용 가능한 테마 목록 조회 |
| GET | `/themes/{themeId}` | 불필요 | 테마 상세 조회 (색상/폰트 옵션 포함) |
| GET | `/mcards/{mcardId}/theme` | 필요 | 청첩장 테마 설정 조회 |
| PUT | `/mcards/{mcardId}/theme` | 필요 | 청첩장 테마 설정 저장 |

---

## 5. 인트로 설정

| 메서드 | 엔드포인트 | 인증 | 설명 |
|--------|-----------|------|------|
| GET | `/intros` | 불필요 | 인트로 레이아웃 스타일 목록 조회 |
| GET | `/mcards/{mcardId}/intro` | 필요 | 청첩장 인트로 스타일 조회 |
| PUT | `/mcards/{mcardId}/intro` | 필요 | 청첩장 인트로 스타일 저장 |

---

## 6. 신랑·신부 정보

| 메서드 | 엔드포인트 | 인증 | 설명 |
|--------|-----------|------|------|
| GET | `/mcards/{mcardId}/couple` | 필요 | 신랑·신부 및 혼주 정보 조회 |
| PUT | `/mcards/{mcardId}/couple` | 필요 | 신랑·신부 및 혼주 정보 저장 |

---

## 7. 모시는 글 (청첩 인사말)

| 메서드 | 엔드포인트 | 인증 | 설명 |
|--------|-----------|------|------|
| GET | `/mcards/{mcardId}/greeting` | 필요 | 인사말 조회 |
| PUT | `/mcards/{mcardId}/greeting` | 필요 | 인사말 저장 |
| GET | `/greetings/samples` | 불필요 | 인사말 샘플 문구 목록 조회 |

---

## 8. 예식 일시

| 메서드 | 엔드포인트 | 인증 | 설명 |
|--------|-----------|------|------|
| GET | `/mcards/{mcardId}/schedule` | 필요 | 예식 일시 조회 |
| PUT | `/mcards/{mcardId}/schedule` | 필요 | 예식 일시 저장 |

---

## 9. 예식 장소

| 메서드 | 엔드포인트 | 인증 | 설명 |
|--------|-----------|------|------|
| GET | `/mcards/{mcardId}/venue` | 필요 | 예식 장소 조회 |
| PUT | `/mcards/{mcardId}/venue` | 필요 | 예식 장소 저장 |
| POST | `/mcards/{mcardId}/venue/transports` | 필요 | 교통수단 안내 추가 |
| PUT | `/mcards/{mcardId}/venue/transports/{transportId}` | 필요 | 교통수단 안내 수정 |
| DELETE | `/mcards/{mcardId}/venue/transports/{transportId}` | 필요 | 교통수단 안내 삭제 |

> `mapImageUrl`은 클라이언트가 전송하지 않는다. `lat`/`lng`가 있으면 서버가 자동으로 네이버 Static Map API를 호출해 PNG 이미지를 생성하고 Cloudflare R2에 저장한다. 생성에 실패하더라도 venue 저장 자체는 성공한다 (기존 URL 유지, 오류는 로그에만 기록).

---

## 9-1. 주소 검색 및 지도 이미지 (카카오/네이버 Maps API 연동)

예식 장소 편집 화면의 주소 자동완성과 지도 이미지 미리보기를 위한 엔드포인트.

| 메서드 | 엔드포인트 | 인증 | 설명 |
|--------|-----------|------|------|
| GET | `/address/search?query={키워드}` | 필요 | 카카오 키워드 검색 — 장소명·도로명·지번 주소 검색 (최대 10건) |
| GET | `/address/map?lat={위도}&lng={경도}&width={px}&height={px}` | 필요 | 네이버 Static Map — 위경도 기반 PNG 이미지 바이너리 직접 반환 |

- `/address/map`은 `Content-Type: image/png`로 바이너리를 직접 반환한다 (JSON 래퍼 없음). 기본 크기 `width=400`, `height=300`.
- 네이버 Static Map URI는 `java.net.URI` 5-arg 생성자로 빌드 (`|` → `%7C` 인코딩 보장, UriComponentsBuilder 우회).

---

## 10. 갤러리

| 메서드 | 엔드포인트 | 인증 | 설명 |
|--------|-----------|------|------|
| GET | `/mcards/{mcardId}/gallery` | 필요 | 갤러리 사진 목록 조회 |
| POST | `/mcards/{mcardId}/gallery` | 필요 | 사진 업로드 (multipart/form-data) |
| PUT | `/mcards/{mcardId}/gallery/order` | 필요 | 사진 순서 변경 |
| DELETE | `/mcards/{mcardId}/gallery/{photoId}` | 필요 | 사진 삭제 |
| PUT | `/mcards/{mcardId}/gallery/layout` | 필요 | 갤러리 레이아웃 설정 저장 |

---

## 11. 연락하기

| 메서드 | 엔드포인트 | 인증 | 설명 |
|--------|-----------|------|------|
| GET | `/mcards/{mcardId}/contacts` | 필요 | 연락처 정보 조회 |
| PUT | `/mcards/{mcardId}/contacts` | 필요 | 연락처 정보 저장 |

---

## 12. 계좌번호

| 메서드 | 엔드포인트 | 인증 | 설명 |
|--------|-----------|------|------|
| GET | `/mcards/{mcardId}/accounts` | 필요 | 계좌 목록 조회 |
| POST | `/mcards/{mcardId}/accounts` | 필요 | 계좌 추가 |
| PUT | `/mcards/{mcardId}/accounts/{accountId}` | 필요 | 계좌 수정 |
| DELETE | `/mcards/{mcardId}/accounts/{accountId}` | 필요 | 계좌 삭제 |

---

## 13. 동영상

| 메서드 | 엔드포인트 | 인증 | 설명 |
|--------|-----------|------|------|
| GET | `/mcards/{mcardId}/video` | 필요 | 동영상 정보 조회 |
| PUT | `/mcards/{mcardId}/video` | 필요 | 동영상 URL 및 제목 저장 |

---

## 14. 배경음악

| 메서드 | 엔드포인트 | 인증 | 설명 |
|--------|-----------|------|------|
| GET | `/mcards/{mcardId}/bgm` | 필요 | 배경음악 설정 조회 |
| PUT | `/mcards/{mcardId}/bgm` | 필요 | 배경음악 설정 저장 |
| POST | `/mcards/{mcardId}/bgm/upload` | 필요 | 배경음악 파일 업로드 (multipart/form-data) |
| GET | `/bgm/samples` | 불필요 | 샘플 음악 목록 조회 |

---

## 15. 안내사항

| 메서드 | 엔드포인트 | 인증 | 설명 |
|--------|-----------|------|------|
| GET | `/mcards/{mcardId}/notices` | 필요 | 안내사항 목록 조회 |
| POST | `/mcards/{mcardId}/notices` | 필요 | 안내사항 추가 |
| PUT | `/mcards/{mcardId}/notices/{noticeId}` | 필요 | 안내사항 수정 |
| DELETE | `/mcards/{mcardId}/notices/{noticeId}` | 필요 | 안내사항 삭제 |
| GET | `/notices/samples` | 불필요 | 안내사항 샘플 문구 목록 조회 |

---

## 16. 참석의사 (RSVP)

| 메서드 | 엔드포인트 | 인증 | 설명 |
|--------|-----------|------|------|
| GET | `/mcards/{mcardId}/rsvp/settings` | 필요 | RSVP 설정 조회 |
| PUT | `/mcards/{mcardId}/rsvp/settings` | 필요 | RSVP 활성/비활성 설정 저장 |
| POST | `/mcards/{mcardId}/rsvp` | 불필요 | 하객 참석의사 응답 제출 |
| GET | `/mcards/{mcardId}/rsvp` | 필요 | RSVP 응답 목록 조회 (제작자 전용) |

---

## 17. 방명록

| 메서드 | 엔드포인트 | 인증 | 설명 |
|--------|-----------|------|------|
| GET | `/mcards/{mcardId}/guestbook/settings` | 필요 | 방명록 설정 조회 |
| PUT | `/mcards/{mcardId}/guestbook/settings` | 필요 | 방명록 활성/비활성 설정 저장 |
| GET | `/mcards/{mcardId}/guestbook` | 불필요 | 방명록 메시지 목록 조회 |
| POST | `/mcards/{mcardId}/guestbook` | 불필요 | 하객 방명록 메시지 작성 |
| DELETE | `/mcards/{mcardId}/guestbook/{messageId}` | 필요 | 방명록 메시지 삭제 (제작자 전용) |

---

## 18. 화환 보내기

| 메서드 | 엔드포인트 | 인증 | 설명 |
|--------|-----------|------|------|
| GET | `/mcards/{mcardId}/wreath` | 필요 | 화환 URL 설정 조회 |
| PUT | `/mcards/{mcardId}/wreath` | 필요 | 화환 URL 설정 저장 |

---

## 19. 글귀

| 메서드 | 엔드포인트 | 인증 | 설명 |
|--------|-----------|------|------|
| GET | `/mcards/{mcardId}/quote` | 필요 | 글귀 조회 |
| PUT | `/mcards/{mcardId}/quote` | 필요 | 글귀 저장 |
| GET | `/quotes/samples` | 불필요 | 글귀 샘플 문구 목록 조회 |

---

## 20. 사진 & 글귀

| 메서드 | 엔드포인트 | 인증 | 설명 |
|--------|-----------|------|------|
| GET | `/mcards/{mcardId}/photo-quote` | 필요 | 사진+글귀 블록 조회 |
| PUT | `/mcards/{mcardId}/photo-quote` | 필요 | 사진+글귀 블록 저장 |
| POST | `/mcards/{mcardId}/photo-quote/upload` | 필요 | 사진+글귀 이미지 업로드 (multipart/form-data) |

---

## 21. 공유 썸네일

| 메서드 | 엔드포인트 | 인증 | 설명 |
|--------|-----------|------|------|
| GET | `/mcards/{mcardId}/thumbnail` | 필요 | 공유 썸네일 설정 조회 |
| PUT | `/mcards/{mcardId}/thumbnail` | 필요 | 공유 썸네일 설정 저장 |
| POST | `/mcards/{mcardId}/thumbnail/upload?type={type}` | 필요 | 썸네일 이미지 업로드 (multipart/form-data) |

> `type` 파라미터: `kakao` (카카오톡 공유용) 또는 `url` (URL 공유용, 기본값)

---

## 22. 메뉴 순서

| 메서드 | 엔드포인트 | 인증 | 설명 |
|--------|-----------|------|------|
| GET | `/mcards/{mcardId}/sections/order` | 필요 | 섹션 노출 순서 조회 |
| PUT | `/mcards/{mcardId}/sections/order` | 필요 | 섹션 노출 순서 저장 |

---

## 23. QR 코드

| 메서드 | 엔드포인트 | 인증 | 설명 |
|--------|-----------|------|------|
| GET | `/mcards/{mcardId}/qrcode` | 필요 | QR 코드 이미지 생성 및 반환 |

> `Content-Type: image/png`, `Content-Disposition: attachment`로 바이너리를 직접 반환한다 (JSON 래퍼 없음).

---

## 24. 파일 업로드 (공통)

| 메서드 | 엔드포인트 | 인증 | 설명 |
|--------|-----------|------|------|
| POST | `/files/upload?folder={folder}` | 필요 | 이미지/파일 업로드 — Cloudflare R2 저장 후 URL 반환 |
| DELETE | `/files?fileUrl={fileUrl}` | 필요 | 업로드 파일 삭제 |

> `folder` 파라미터: `gallery` / `bgm` / `photo-quote` / `thumbnail` / `test`. 요청 필드명: `file` (multipart/form-data).

---

## 25. 헬스 체크

| 메서드 | 엔드포인트 | 인증 | 설명 |
|--------|-----------|------|------|
| GET | `/health` | 불필요 | 서버 상태 확인 — Docker/Nginx/CI-CD 공통 사용 |

> `serverIp`, `hostname` 필드를 포함하므로 로드밸런서 분산 검증에 활용할 수 있다.

---

## 환경 변수 목록

| 변수명 | 설명 |
|--------|------|
| `KAKAO_CLIENT_ID` | 카카오 OAuth 앱 키 |
| `KAKAO_CLIENT_SECRET` | 카카오 OAuth 시크릿 |
| `KAKAO_REDIRECT_URI` | 카카오 콜백 URL |
| `CLOUDFLARE_ACCOUNT_ID` | R2 계정 ID |
| `CLOUDFLARE_ACCESS_KEY_ID` | R2 액세스 키 |
| `CLOUDFLARE_SECRET_ACCESS_KEY` | R2 시크릿 키 |
| `CLOUDFLARE_BUCKET_NAME` | R2 버킷명 |
| `CLOUDFLARE_BUCKET_URL` | R2 퍼블릭 URL (`https://...`) |
| `JWT_SECRET` | JWT 서명 비밀키 |
| `NAVER_MAP_CLIENT_ID` | 네이버 클라우드 플랫폼 Maps Application Client ID |
| `NAVER_MAP_CLIENT_SECRET` | 네이버 클라우드 플랫폼 Maps Application Client Secret |
| `APP_BASE_URL` | 서비스 기본 URL — QR 코드 생성 시 사용 (예: `https://mcard.example.com`) |
| `SPRING_PROFILES_ACTIVE` | 활성 프로필 (`local` / `dev` / `prd`) |
