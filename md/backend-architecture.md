# 모바일 청첩장 서비스 - 백엔드 아키텍처

> 작성일: 2026-05-03

---

## 1. 개요

모바일 청첩장 서비스의 백엔드 아키텍처는 **마이크로서비스 아키텍처**를 기반으로 하며,
**Docker 컨테이너화**를 통해 배포된다. 운영 환경과 개발 환경을 분리하여
안정적인 서비스 운영을 목표로 한다.

---

## 2. 기술 스택

### Backend
- **Framework**: Spring Boot 4.0.6
- **Language**: Java 17
- **Database**: MySQL 8.0
- **ORM**: JPA (Hibernate)
- **Authentication**: 카카오 OAuth 2.0 소셜 로그인 + 자체 JWT 발급 (Authorization: Bearer)
- **File Storage**: Cloudflare R2
- **Build Tool**: Gradle

### Infrastructure
- **Container**: Docker
- **Load Balancer**: Nginx
- **Reverse Proxy**: Nginx
- **Monitoring**: (추후 도입 예정)

---

## 3. 아키텍처 구성

### 3-1. 서버 분리 전략

서비스는 **환경별로 완전히 분리된 서버 구조**를 채택한다:

```
[운영 환경]
운영 도메인 (prod.example.com)
    ↓ (DNS 라우팅)
Docker Nginx Load Balancer
    ↓ (로드 밸런싱)
├── 운영 서버 1 (prod-app-01)
├── 운영 서버 2 (prod-app-02)
└── 운영 서버 N (prod-app-N)

[개발 환경]
개발 도메인 (dev.example.com)
    ↓ (DNS 라우팅)
Docker Nginx Load Balancer
    ↓ (로드 밸런싱)
├── 개발 서버 1 (dev-app-01)
├── 개발 서버 2 (dev-app-02)
└── 개발 서버 N (dev-app-N)
```

**특징:**
- **도메인 분리**: prod.example.com ↔ dev.example.com 완전 분리
- **환경 격리**: 운영/개발 환경의 완전한 분리
- **수평 확장**: 서버 증설 시 로드 밸런서 설정만으로 가능
- **무중단 배포**: 서버별 순차적 배포로 서비스 중단 방지

### 3-2. 단일 서버 아키텍처

각 서버는 다음과 같은 구조로 구성된다:

```
[Docker Container]
┌─────────────────────────────────────┐
│              Nginx                  │
│          (Reverse Proxy)            │
│                                     │
│  • SSL Termination                  │
│  • Request Routing                  │
│  • Static File Serving              │
│  • Rate Limiting                    │
└─────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────┐
│        Spring Boot App              │
│                                     │
│  • REST API                         │
│  • Business Logic                   │
│  • Database Connection              │
│  • File Upload/Download             │
└─────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────┐
│            MySQL                    │
│                                     │
│  • User Data                        │
│  • Mcard Data                       │
│  • File Metadata                    │
└─────────────────────────────────────┘
```

---

## 4. 배포 전략

### 4-1. Docker 구성

**Dockerfile (Spring Boot)**
```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/*.jar app.jar

EXPOSE 8080

# Docker 레벨 헬스 체크 (컨테이너 자체 상태 확인)
HEALTHCHECK --interval=10s --timeout=3s --start-period=30s --retries=3 \
  CMD curl -f http://localhost:8080/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Docker Compose (개발용)**
```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=dev
    depends_on:
      - mysql
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/health"]
      interval: 10s
      timeout: 3s
      retries: 3
      start_period: 30s

  mysql:
    image: mysql:8.0
    environment:
      - MYSQL_ROOT_PASSWORD=password
      - MYSQL_DATABASE=wedding_invitation
    ports:
      - "3306:3306"
```

### 4-2. Nginx 설정

**Load Balancer 설정 (/etc/nginx/nginx.conf)**
```nginx
upstream prod_backend {
    least_conn;  # 현재 활성 연결 수가 가장 적은 서버로 라우팅 (쏠림 방지)
    server prod-app-01:8080;
    server prod-app-02:8080;
    server prod-app-03:8080;
}

server {
    listen 80;
    server_name prod.example.com;

    location / {
        proxy_pass http://prod_backend;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # Health Check
    location /health {
        proxy_pass http://prod_backend;
        access_log off;
    }
}
```

### 4-3. 환경별 설정

**운영 환경 변수**
```bash
# Database
MYSQL_HOST=prod-db.example.com
MYSQL_PORT=3306
MYSQL_DATABASE=wedding_invitation_prod
MYSQL_USER=prod_user
MYSQL_PASSWORD=prod_password

# Kakao OAuth2
KAKAO_CLIENT_ID=prod_kakao_client_id
KAKAO_CLIENT_SECRET=prod_kakao_client_secret
KAKAO_REDIRECT_URI=https://prod.example.com/api/v1/auth/kakao/callback

# Cloudflare R2
CLOUDFLARE_ACCOUNT_ID=prod_account_id
CLOUDFLARE_ACCESS_KEY_ID=prod_access_key
CLOUDFLARE_SECRET_ACCESS_KEY=prod_secret_key
CLOUDFLARE_BUCKET_NAME=prod-wedding-bucket

# Domain
APP_DOMAIN=https://prod.example.com
```

**개발 환경 변수**
```bash
# Database
MYSQL_HOST=dev-db.example.com
MYSQL_PORT=3306
MYSQL_DATABASE=wedding_invitation_dev
MYSQL_USER=dev_user
MYSQL_PASSWORD=dev_password

# Kakao OAuth2
KAKAO_CLIENT_ID=dev_kakao_client_id
KAKAO_CLIENT_SECRET=dev_kakao_client_secret
KAKAO_REDIRECT_URI=http://localhost:8080/api/v1/auth/kakao/callback

# Cloudflare R2
CLOUDFLARE_ACCOUNT_ID=dev_account_id
CLOUDFLARE_ACCESS_KEY_ID=dev_access_key
CLOUDFLARE_SECRET_ACCESS_KEY=dev_secret_key
CLOUDFLARE_BUCKET_NAME=dev-wedding-bucket

# Domain
APP_DOMAIN=https://dev.example.com
```

### 4-4. 헬스 체크 기반 배포 검증

배포 시 Health Check API의 응답 결과에 따라 **자동 롤백** 여부가 결정된다. 신규 컨테이너가 정상적으로 기동되었는지 확인한 뒤에만 트래픽을 전환하며, 실패 시 즉시 이전 버전으로 복구한다.

**검증 절차:**

```
1. 신규 이미지 Pull
2. 신규 컨테이너 기동 (이전 컨테이너는 유지)
3. start-period (30초) 대기 - 애플리케이션 부팅 시간 확보
4. /health 엔드포인트 polling
   • 간격: 5초
   • 최대 시도: 12회 (총 60초)
   • 성공 조건: HTTP 200 + status=UP
5. 검증 성공 시 → 트래픽 전환 (Nginx upstream 갱신)
6. 검증 실패 시 → 신규 컨테이너 종료 → 이전 버전 유지 (자동 롤백)
```

---

## 5. 데이터베이스 아키텍처

### 5-1. 데이터베이스 분리

```
[운영 환경]
prod-db.example.com:3306/wedding_invitation_prod

[개발 환경]
dev-db.example.com:3306/wedding_invitation_dev
```

### 5-2. 데이터베이스 스키마

**주요 테이블:**
- `users`: 사용자 정보
- `mcards`: 청첩장 기본 정보
- `mcard_themes`: 테마 설정
- `mcard_couples`: 신랑/신부 정보
- `gallery_photos`: 갤러리 사진
- `rsvp_responses`: 참석의사 응답
- `guestbook_messages`: 방명록 메시지

### 5-3. 백업 전략

- **일일 백업**: 자동화된 데이터베이스 백업
- **증분 백업**: 변경분만 백업하여 저장 공간 최적화
- **재해 복구**: 다중 리전 백업 저장

---

## 6. 보안 아키텍처

### 6-1. 네트워크 보안

- **SSL/TLS**: 모든 통신에 HTTPS 적용
- **Firewall**: 서버별 방화벽 설정
- **VPC**: AWS VPC를 통한 네트워크 격리

### 6-2. 애플리케이션 보안

- **카카오 OAuth 2.0 인증**: 카카오 소셜 로그인으로 사용자 인증
- **자체 JWT 발급**: 카카오 로그인 성공 시 서버가 HS256 JWT 생성 (payload: userId, 만료 7일)
- **JWT 전달**: 로그인 응답 body(`accessToken`)로 반환 → 클라이언트가 저장 후 `Authorization: Bearer` 헤더로 전송
- **JWT Filter**: 모든 인증 필요 요청에서 JWT 서명 검증 후 SecurityContext에 userId 저장
- **CORS 설정**: 도메인별 접근 제어
- **Rate Limiting**: Nginx를 통한 요청 제한
- **Input Validation**: 모든 입력 데이터 검증

### 6-3. 데이터 보안

- **암호화**: 민감 데이터 암호화 저장
- **접근 제어**: 데이터베이스 레벨 접근 제어
- **로그 관리**: 보안 이벤트 로깅

---

## 7. 모니터링 및 로깅

### 7-1. 헬스 체크

서버의 정상 동작 여부를 확인하기 위한 Health Check API를 제공한다. 이 엔드포인트는 **로드 밸런서, Docker, 배포 파이프라인** 모두에서 공통으로 사용되며 배포 검증의 핵심 기준이 된다.

**엔드포인트:**
- **URL**: `GET /health`
- **인증**: 불필요 (public)
- **컨트롤러**: `HealthController`

**응답 형식 (정상)**
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

**응답 필드:**
- `status`: 서버 상태 (`UP` / `DOWN`)
- `timestamp`: 응답 시각
- `service`: 서비스 식별자
- `version`: 현재 배포된 애플리케이션 버전

**판정 기준:**
- HTTP `200 OK` + `status: "UP"` → 정상
- 그 외 응답 코드 또는 응답 누락 → 비정상 (배포 실패로 간주)

**활용처:**
- Docker `HEALTHCHECK`: 컨테이너 자체 상태 모니터링
- Nginx upstream: 비정상 서버는 트래픽 분산 대상에서 제외
- CI/CD 파이프라인: 배포 검증 및 자동 롤백 트리거

### 7-2. 로깅

- **애플리케이션 로그**: Spring Boot 기본 로깅
- **접근 로그**: Nginx access log
- **에러 로그**: 별도 에러 로그 파일
- **로그 수집**: ELK Stack (추후 도입)

### 7-3. 모니터링

- **메트릭 수집**: Spring Boot Actuator
- **알림**: 서버 다운, 높은 CPU/메모리 사용률
- **대시보드**: Grafana (추후 도입)

---

## 8. 확장성 및 성능

### 8-1. 수평 확장

- **Auto Scaling**: 트래픽에 따른 서버 자동 증설
- **Load Balancing**: Nginx를 통한 부하 분산
- **Database Sharding**: 데이터 증가 시 샤딩 고려

### 8-2. 캐싱 전략

- **Redis**: 세션, 자주 조회되는 데이터 캐싱
- **CDN**: Cloudflare를 통한 정적 파일 캐싱
- **Database Query Cache**: JPA 2차 캐시

### 8-3. 성능 최적화

- **Connection Pool**: HikariCP 사용
- **Lazy Loading**: JPA 지연 로딩
- **인덱싱**: 데이터베이스 인덱스 최적화

---

## 9. 배포 프로세스

### 9-1. CI/CD 파이프라인

```
Git Push → Build → Test → Docker Build → Deploy → Health Check → (성공) Traffic Switch
                                                       ↓
                                                    (실패)
                                                       ↓
                                                    Rollback
```

| 단계 | 설명 |
| --- | --- |
| Source Code | Git Push (dev / main 브랜치 트리거) |
| Build | Gradle 빌드 (`./gradlew clean build`) |
| Test | 단위/통합 테스트 |
| Docker Build | 이미지 빌드 및 Container Registry 푸시 |
| Deploy | 신규 컨테이너 기동 (이전 버전 유지) |
| **Health Check** | **`/health` 검증 → 실패 시 자동 롤백** |
| Traffic Switch | Blue-Green 방식 트래픽 전환 |

### 9-2. 무중단 배포 (Blue-Green) 및 자동 롤백

배포는 항상 **이전 버전(Blue)을 유지한 채 신규 버전(Green)을 기동**하여, 검증이 끝난 뒤에만 트래픽을 전환한다. 검증 단계에서 Health Check가 실패하면 신규 버전을 종료하고 이전 버전을 그대로 유지하므로 서비스 중단이 발생하지 않는다.

**배포 플로우:**

```
[Step 1] 이전 버전 (Blue) 유지        ──→ 트래픽 100% 처리 중
              │
[Step 2] 신규 버전 (Green) 컨테이너 기동
              │
[Step 3] start-period 대기 (30초)
              │
[Step 4] /health 엔드포인트 검증
              │
        ┌─────┴─────┐
        ↓           ↓
   [성공]        [실패]
        │           │
        ↓           ↓
[Step 5a]      [Step 5b]
Nginx upstream  Green 컨테이너
갱신 → Green    종료
으로 트래픽         │
전환                ↓
        │      Blue 유지
        ↓      (자동 롤백 완료)
[Step 6a]           │
Blue 컨테이너        ↓
종료            배포 실패 알림
        │      (Slack / Email)
        ↓
   배포 완료
```

**Health Check 검증 상세:**
- **검증 대상 URL**: `http://<신규컨테이너>:8080/health`
- **검증 간격**: 5초
- **최대 재시도**: 12회 (총 60초 대기)
- **성공 조건**: HTTP 200 응답 + `status == "UP"`
- **실패 시 동작**: 신규 컨테이너 즉시 종료 → 이전 버전 트래픽 유지

**배포 스크립트 예시 (`deploy.sh`)**
```bash
#!/bin/bash
set -e

ENV=${1:-dev}                # dev | prod
NEW_IMAGE="wedding-backend:${BUILD_NUMBER}"
OLD_CONTAINER="wedding-backend-${ENV}-current"
NEW_CONTAINER="wedding-backend-${ENV}-candidate"

echo "[1/4] 신규 컨테이너 기동..."
docker run -d --name "$NEW_CONTAINER" \
  --env-file ".env.${ENV}" \
  -p 8081:8080 \
  "$NEW_IMAGE"

echo "[2/4] 부팅 대기 (30초)..."
sleep 30

echo "[3/4] Health Check 검증..."
HEALTH_OK=false
for i in $(seq 1 12); do
  RESPONSE=$(curl -s -o /tmp/health.json -w "%{http_code}" http://localhost:8081/health || echo "000")
  STATUS=$(jq -r '.data.status // "DOWN"' /tmp/health.json 2>/dev/null || echo "DOWN")

  if [ "$RESPONSE" = "200" ] && [ "$STATUS" = "UP" ]; then
    HEALTH_OK=true
    echo "  ✓ Health Check 성공 (시도 $i/12)"
    break
  fi

  echo "  ✗ Health Check 실패 (시도 $i/12, HTTP=$RESPONSE, status=$STATUS)"
  sleep 5
done

if [ "$HEALTH_OK" = false ]; then
  echo "[ROLLBACK] Health Check 실패 → 신규 컨테이너 종료, 이전 버전 유지"
  docker stop "$NEW_CONTAINER" && docker rm "$NEW_CONTAINER"
  # Slack 알림 등
  exit 1
fi

echo "[4/4] 트래픽 전환..."
# Nginx upstream 갱신 후 reload
./switch-traffic.sh "$NEW_CONTAINER"
docker stop "$OLD_CONTAINER" && docker rm "$OLD_CONTAINER"
docker rename "$NEW_CONTAINER" "$OLD_CONTAINER"

echo "배포 완료: $NEW_IMAGE"
```

### 9-3. 롤백 전략

롤백은 **자동 롤백**과 **수동 롤백** 두 가지 경로를 제공한다.

**(1) 자동 롤백 — 배포 직후 Health Check 실패**
- 트리거: `/health` 검증 실패
- 동작: 신규 컨테이너 종료, 이전 버전 트래픽 유지
- 소요 시간: 평균 1~2분 이내
- 데이터 영향: 없음 (트래픽 전환 전이므로)

**(2) 수동 롤백 — 배포 후 운영 중 이상 발견**
- 트리거: 운영자 판단 (에러율 증가, 응답 지연 등)
- 동작: 이전 이미지 태그로 재배포 (위 9-2 플로우 동일하게 진행)
- 데이터 롤백: DB 마이그레이션 역방향 스크립트 실행 필요 시
- 모니터링: 배포 후 30분간 집중 모니터링

**롤백 보존 정책:**
- Container Registry에 최근 **5개 버전** 이미지 보존
- 각 환경(dev/prod) 별로 직전 버전을 별도 태그(`:previous`)로 유지

---

## 10. 장애 대응

### 10-1. 장애 유형별 대응

- **서버 다운**: 로드 밸런서가 자�