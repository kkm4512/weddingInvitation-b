# 모바일 청첩장 서비스 - 백엔드 API 명세

> 기준: React(Frontend) ↔ Spring Boot(Backend) RESTful API  
> 인증: 카카오 OAuth 2.0 (Spring Security OAuth2 Client 세션 기반)  
> Base URL: `/api/v1`

---

## 1. 인증 (Authentication)

| 메서드 | 엔드포인트 | 설명 |
|--------|-----------|------|
| GET | `/auth/kakao` | 카카오 OAuth 로그인 시작 |
| GET | `/auth/kakao/callback` | 카카오 OAuth 콜백 처리 및 세션 발급 |
| POST | `/auth/logout` | 로그아웃 (세션 무효화) |
| GET | `/auth/me` | 현재 로그인 사용자 정보 조회 |

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
| GET | `/themes` | 사용 가능한 테마 목록 조회 |
| GET | `/themes/{themeId}` | 테마 상세 조회 (색상/폰트 옵션 포함) |
| PUT | `/mcards/{mcardId}/theme` | 청첩장 테마 설정 저장 |

---

## 5. 인트로 설정

| 메서드 | 엔드포인트 | 설명 |
|--------|-----------|------|
| GET | `/intros` | 인트로 레이아웃 스타일 목록 조회 |
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
| POST | `/mcards/{mcardId}/thumbnail/upload` | 썸네일 이미지 업로드 |

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
| POST | `/files/upload` | 이미지/파일 업로드 (S3 저장 후 URL 반환) |
| DELETE | `/files` | 업로드 파일 삭제 |

---

## 25. 결제

| 메서드 | 엔드포인트 | 설명 |
|--------|-----------|------|
| POST | `/payments` | 결제 요청 (PG사 연동) |
| POST | `/payments/webhook` | PG사 결제 Webhook 수신 처리 |
| GET | `/payments/{paymentId}` | 결제 내역 조회 |
| GET | `/mcards/{mcardId}/payment-status` | 청첩장 결제 상태 조회 (워터마크 여부) |

---

## 26. 헬스 체크

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

- 모든 인증 필요 API는 Spring Security 세션 쿠키 기반으로 처리 (카카오 OAuth 2.0 로그인 후 세션 발급)
- 하객용 API (`/w/{inviteCode}`, RSVP 제출, 방명록 작성)는 인증 불필요
- `/health` 엔드포인트는 인증 없이 접근 가능 (Docker, Nginx, CI/CD 배포 검증용)
- 파일 업로드는 `multipart/form-data` 사용
- 페이징이 필요한 목록 API는 `?page=0&size=20` 쿼리 파라미터 적용 권장

---

_작성일: 2026-05-03_
