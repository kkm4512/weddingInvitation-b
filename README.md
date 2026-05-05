# 💍 Wedding Invitation — Backend Server

모바일 청첩장 서비스의 Spring Boot 백엔드 서버입니다.

---

## 기술 스택

| 분류 | 기술 |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.0.6 |
| Database | MySQL 8.0 (Aiven Cloud) |
| ORM | Spring Data JPA / Hibernate |
| 인증 | Kakao OAuth 2.0 + JWT (HS256, 7일) |
| 파일 스토리지 | Cloudflare R2 (S3 호환) |
| 지도 | Naver Static Map API |
| QR 코드 | ZXing |
| 배포 | Render (Docker) |
| 프론트엔드 | Vercel (별도 레포) |

---

## 프로젝트 구조

```
src/main/java/com/example/weddingInvitation_b/
├── client/          # 외부 API 클라이언트 (Kakao OAuth)
├── config/          # Security, CORS, S3 등 설정
├── controller/      # REST 컨트롤러 (80개 API)
├── domain/          # JPA 엔티티
├── dto/             # Request / Response DTO
│   ├── request/
│   └── response/
├── exception/       # 글로벌 예외 처리
├── repository/      # Spring Data JPA 레포지토리
├── service/         # 서비스 인터페이스 + impl
└── util/            # JwtProvider 등 유틸
```

---

## 환경 변수 설정

`envs/` 디렉토리 안에 프로파일별 `.env` 파일을 작성합니다.
각 파일은 `.gitignore`에 포함되어 있으므로 **절대 커밋하지 마세요.**

| 파일 | 프로파일 | 용도 |
|---|---|---|
| `envs/local.env` | `local` | 로컬 개발 (localhost DB) |
| `envs/dev.env` | `dev` | 개발 서버 (Render + Aiven) |
| `envs/prd.env` | `prd` | 운영 서버 |

### 필요한 환경 변수 목록

```env
# Spring 프로파일
SPRING_PROFILES_ACTIVE=local

# Database (로컬: localhost, dev/prd: Aiven)
DB_URL=jdbc:mysql://localhost:3306/wedding_invitation?useSSL=false&serverTimezone=Asia/Seoul&...
DB_USERNAME=root
DB_PASSWORD=root

# Kakao OAuth2
KAKAO_CLIENT_ID=
KAKAO_CLIENT_SECRET=
KAKAO_REDIRECT_URI=http://localhost:8080/api/v1/auth/kakao/callback

# JWT (HS256, 최소 32자)
JWT_SECRET=

# Cloudflare R2
CLOUDFLARE_ACCOUNT_ID=
CLOUDFLARE_ACCESS_KEY_ID=
CLOUDFLARE_SECRET_ACCESS_KEY=
CLOUDFLARE_BUCKET_NAME=wedding-invitation
CLOUDFLARE_BUCKET_URL=https://{account_id}.r2.cloudflarestorage.com/{bucket}
CLOUDFLARE_PUBLIC_URL=https://pub-xxx.r2.dev

# Naver Static Map API
NAVER_MAP_CLIENT_ID=
NAVER_MAP_CLIENT_SECRET=

# JPA
DDL_AUTO=update
SHOW_SQL=true

# Server
SERVER_PORT=8080

# 프론트엔드 URL (CORS + OAuth 리다이렉트 대상)
APP_URL=http://localhost:5173
```

---

## 로컬 실행

### 1. MySQL 실행

```bash
# Docker로 빠르게 띄우는 경우
docker run -d \
  --name wedding-db \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=wedding_invitation \
  -p 3306:3306 \
  mysql:8.0
```

### 2. 환경 변수 파일 작성

```bash
# envs/local.env 를 위 목록 참고해서 작성
```

### 3. 빌드 & 실행

```bash
# Gradle 빌드 (테스트 제외)
./gradlew clean build -x test

# 실행
java -jar build/libs/weddingInvitation_b-0.0.1-SNAPSHOT.jar
```

서버 기동 후 [http://localhost:8080/health](http://localhost:8080/health) 에서 상태를 확인할 수 있습니다.

---

## 인증 흐름

```
[프론트엔드]                        [백엔드]                      [카카오]
     │                                 │                              │
     │  GET /api/v1/auth/kakao         │                              │
     │────────────────────────────────▶│                              │
     │◀── { redirectUrl: "https://kauth.kakao.com/..." }             │
     │                                 │                              │
     │  window.location.href = redirectUrl                            │
     │──────────────────────────────────────────────────────────────▶│
     │                                 │                              │
     │◀── 302 redirect ?code=xxx ───────────────────────────────────│
     │                                 │                              │
     │  GET /api/v1/auth/kakao/callback?code=xxx                      │
     │────────────────────────────────▶│                              │
     │                                 │── 토큰 교환, 사용자 정보 조회 ──▶│
     │                                 │◀──────────────────────────────│
     │                                 │  User 조회 or 신규 생성        │
     │                                 │  JWT 발급 (7일)               │
     │◀── 302 {APP_URL}/auth/callback?accessToken=JWT&userId=1 ──────│
     │                                 │                              │
     │  이후 모든 API: Authorization: Bearer {JWT}
```

---

## API 구성

총 **80개 API**가 카테고리별로 구성되어 있습니다.
상세 명세는 [`document/weddingInvitation_server_spec_0.0.2v.xlsx`](document/weddingInvitation_server_spec_0.0.2v.xlsx) 참고.

| 카테고리 | 주요 엔드포인트 |
|---|---|
| 인증 | `GET /api/v1/auth/kakao`, `/auth/kakao/callback`, `/auth/me` |
| 청첩장 | `GET/POST/PUT/DELETE /api/v1/mcards/{mcardId}` |
| 신랑신부 | `/mcards/{mcardId}/couple` |
| 예식 일시 | `/mcards/{mcardId}/schedule` |
| 갤러리 | `/mcards/{mcardId}/gallery` (업로드/순서/레이아웃) |
| 예식 장소 | `/mcards/{mcardId}/venue` + 교통수단 |
| RSVP | `/mcards/{mcardId}/rsvp` + settings |
| 방명록 | `/mcards/{mcardId}/guestbook` |
| 공개 뷰 | `GET /api/v1/w/{inviteCode}` (인증 불필요, 하객용) |
| 헬스체크 | `GET /health` (UptimeRobot 핑 대상) |

---

## 외부 서비스 의존성

| 서비스 | 용도 |
|---|---|
| **Kakao OAuth 2.0** | 소셜 로그인 — 인증 코드 교환 및 사용자 정보 조회 |
| **Cloudflare R2** | 갤러리 사진, BGM, 썸네일, 사진글귀 등 파일 저장. 엔티티 삭제 시 R2 파일도 함께 삭제 |
| **Naver Static Map API** | 예식 장소 저장 시 위경도(lat/lng) 기반으로 지도 이미지를 자동 생성해 R2에 저장 |
| **Aiven MySQL** | 관리형 MySQL 8.0 클라우드 DB (dev/prd 환경) |

---

## 배포 구조

```
[Vercel - 프론트엔드]
        │  HTTPS API 호출
        ▼
[Render - 백엔드]
  Docker 컨테이너 (eclipse-temurin:17-jre-alpine)
  Port 8080
  HEALTHCHECK: GET /health (10s 간격)
        │
        ├── Aiven MySQL 8.0 (SSL)
        └── Cloudflare R2 (S3 API)
```

- Render에 환경 변수는 대시보드 또는 `dev.env` / `prd.env` 참고해서 직접 등록
- Blue-Green 배포 방식 사용 (무중단 배포)

### Docker 빌드

```bash
docker build -t wedding-invitation-b .
docker run -p 8080:8080 --env-file envs/local.env wedding-invitation-b
```

---

## 주요 비즈니스 로직 특이사항

- **장소 저장 시 지도 이미지 자동 생성** — `PUT /venue` 호출 시 lat/lng로 Naver Static Map 이미지를 생성하고 R2에 저장한 뒤 `mapImageUrl`에 반영
- **파일 삭제 연동** — 갤러리 사진, BGM 등 파일이 포함된 엔티티 삭제 시 DB 레코드와 R2 오브젝트를 함께 삭제
- **공개 뷰 인증 불필요** — `GET /w/{inviteCode}` 는 인증 없이 접근 가능 (하객용). 나머지 편집 API는 모두 JWT 필요
- **계좌 JSON 키 유연성** — 계좌 요청 바디에서 `accountType` / `side` 둘 다 허용 (`@JsonAlias`)
- **방명록/RSVP 소프트 삭제** — 실제 삭제 대신 `deleted` 플래그 처리
