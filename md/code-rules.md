# 모바일 청첩장 서비스 - 백엔드 코드 작성 규칙

> 적용 대상: Java Spring Boot 백엔드 전체  
> 모든 개발자는 아래 규칙을 준수하여 코드를 작성한다.

---

## 1. 3 레이어 아키텍처 원칙

모든 기능은 **Controller → Service → Repository** 3계층으로 분리하여 구현한다.  
계층 간 역할을 명확히 구분하고, 계층을 건너뛰는 직접 호출은 금지한다.

---

### 1-1. Controller 계층

**역할**
- HTTP 요청 수신 및 응답 반환
- 요청 데이터 유효성 검증 (`@Valid`)
- 인증/인가 처리 (카카오 OAuth 2.0 세션)
- 비즈니스 로직을 직접 구현하지 않고 Service에 위임

**규칙**
- 클래스명: `{도메인}Controller` (예: `McardController`)
- 패키지: `com.project.controller`
- `@RestController`, `@RequestMapping` 필수 선언
- 메서드는 반드시 `ApiResponse<?>` 반환
- Service를 생성자 주입(`@RequiredArgsConstructor`)으로만 사용

**예시**
```java
/**
 * 청첩장 관련 API 컨트롤러
 * - 청첩장 CRUD 요청을 수신하고 McardService에 처리를 위임한다.
 */
@RestController
@RequestMapping("/api/v1/mcards")
@RequiredArgsConstructor
public class McardController {

    private final McardService mcardService;

    /**
     * 청첩장 단건 조회
     * @param mcardId 조회할 청첩장 ID
     * @return 청첩장 상세 데이터 (McardResponseDto)
     */
    @GetMapping("/{mcardId}")
    public ApiResponse<McardResponseDto> getMcard(@PathVariable Long mcardId) {
        McardResponseDto data = mcardService.getMcard(mcardId);
        return ApiResponse.success(data);
    }
}
```

---

### 1-2. Service 계층

**역할**
- 핵심 비즈니스 로직 구현
- 트랜잭션 관리 (`@Transactional`)
- 여러 Repository를 조합하여 처리
- DTO ↔ Entity 변환

**규칙**
- 인터페이스와 구현체를 분리하여 작성
  - 인터페이스: `{도메인}Service` (예: `McardService`)
  - 구현체: `{도메인}ServiceImpl` (예: `McardServiceImpl`)
- 패키지: `com.project.service` / `com.project.service.impl`
- 조회 메서드: `@Transactional(readOnly = true)` 필수 적용
- 변경 메서드: `@Transactional` 적용
- Repository를 생성자 주입으로만 사용

**예시**
```java
/**
 * 청첩장 비즈니스 로직 서비스 인터페이스
 */
public interface McardService {
    McardResponseDto getMcard(Long mcardId);
    McardResponseDto createMcard(McardCreateRequestDto requestDto, Long userId);
    McardResponseDto updateMcard(Long mcardId, McardUpdateRequestDto requestDto);
    void deleteMcard(Long mcardId);
}

/**
 * 청첩장 서비스 구현체
 * - McardRepository를 통해 청첩장 데이터를 처리한다.
 * - Entity와 DTO 간 변환 로직을 담당한다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class McardServiceImpl implements McardService {

    private final McardRepository mcardRepository;

    /**
     * 청첩장 단건 조회
     * - mcardId로 청첩장을 조회하고, 없을 경우 예외를 발생시킨다.
     * @param mcardId 조회할 청첩장 ID
     * @return 청첩장 응답 DTO
     * @throws EntityNotFoundException 청첩장이 존재하지 않을 경우
     */
    @Override
    public McardResponseDto getMcard(Long mcardId) {
        Mcard mcard = mcardRepository.findById(mcardId)
            .orElseThrow(() -> new EntityNotFoundException("청첩장을 찾을 수 없습니다. id=" + mcardId));
        return McardResponseDto.from(mcard);
    }
}
```

---

### 1-3. Repository 계층

**역할**
- DB 접근 전담 (JPA, QueryDSL 등)
- 데이터 조회/저장/수정/삭제만 담당
- 비즈니스 로직 포함 금지

**규칙**
- 인터페이스명: `{도메인}Repository` (예: `McardRepository`)
- 패키지: `com.project.repository`
- Spring Data JPA `JpaRepository` 상속 기본
- 복잡한 쿼리는 QueryDSL 또는 `@Query` 사용
- 메서드명은 Spring Data JPA 네이밍 컨벤션 준수

**예시**
```java
/**
 * 청첩장 데이터 접근 레파지토리
 * - JpaRepository를 상속하여 기본 CRUD를 제공한다.
 * - 커스텀 쿼리가 필요한 경우 @Query 또는 QueryDSL을 사용한다.
 */
public interface McardRepository extends JpaRepository<Mcard, Long> {

    /**
     * 사용자 ID로 청첩장 목록 조회
     * @param userId 사용자 ID
     * @return 해당 사용자의 청첩장 목록
     */
    List<Mcard> findByUserId(Long userId);

    /**
     * 초대 코드로 청첩장 조회 (하객 공개 뷰용)
     * @param inviteCode 초대 코드
     * @return 청첩장 Optional
     */
    Optional<Mcard> findByInviteCode(String inviteCode);
}
```

---

## 2. SOLID 원칙

---

### 2-1. 단일 책임 원칙 (SRP - Single Responsibility Principle)

> 하나의 클래스는 하나의 책임만 가진다.

**적용 방법**
- Controller는 HTTP 처리만, Service는 비즈니스 로직만, Repository는 DB 접근만 담당
- 이메일 발송, 파일 업로드, QR 코드 생성 등은 별도 전용 클래스로 분리

```java
// ✅ 올바른 예 - 파일 업로드 책임을 별도 클래스로 분리
@Component
public class CloudflareR2FileUploader {
    /** Cloudflare R2에 파일을 업로드하고 접근 URL을 반환한다. */
    public String upload(MultipartFile file) { ... }
}

// ❌ 잘못된 예 - 서비스에 파일 업로드 로직을 직접 구현
public class GalleryServiceImpl {
    public void savePhoto(MultipartFile file) {
        // Cloudflare R2 업로드 로직을 여기에 직접 구현 (책임 혼재) ❌
        S3Client r2Client = ...;
        r2Client.putObject(...);
    }
}
```

---

### 2-2. 개방-폐쇄 원칙 (OCP - Open/Closed Principle)

> 확장에는 열려 있고, 수정에는 닫혀 있어야 한다.

**적용 방법**
- 소셜 로그인 제공자 추가 시 기존 코드 수정 없이 새 구현체만 추가
- 인터페이스 기반으로 설계하여 신규 기능은 구현체 추가로 처리

```java
/**
 * 소셜 로그인 처리 인터페이스
 * - 새로운 소셜 로그인 제공자 추가 시 이 인터페이스를 구현하는 클래스만 추가한다.
 * - 기존 코드는 수정하지 않는다. (OCP 준수)
 */
public interface SocialLoginProvider {
    SocialUserInfo getUserInfo(String accessToken);
    String getProviderName();
}

/** 카카오 로그인 구현체 */
@Component
public class KakaoLoginProvider implements SocialLoginProvider { ... }

/** 구글 로그인 구현체 */
@Component
public class GoogleLoginProvider implements SocialLoginProvider { ... }

/** 네이버 로그인 구현체 (신규 추가 시 기존 코드 수정 불필요) */
@Component
public class NaverLoginProvider implements SocialLoginProvider { ... }
```

---

### 2-3. 리스코프 치환 원칙 (LSP - Liskov Substitution Principle)

> 자식 클래스는 부모 클래스를 대체할 수 있어야 한다.

**적용 방법**
- 인터페이스 구현체는 인터페이스 계약(입력/출력/예외)을 반드시 준수
- 상속 시 부모의 동작을 변경하거나 예외를 임의로 추가하지 않음

```java
/**
 * 파일 저장소 인터페이스
 * - 구현체(CloudflareR2, Local)는 동일한 계약을 준수해야 한다.
 * - upload()는 항상 접근 가능한 URL 문자열을 반환해야 한다. (LSP 준수)
 */
public interface FileStorage {
    String upload(MultipartFile file);
    void delete(String fileUrl);
}

@Component
public class CloudflareR2FileStorage implements FileStorage {
    /** Cloudflare R2에 파일을 업로드하고 URL을 반환한다. */
    @Override
    public String upload(MultipartFile file) { ... }

    /** Cloudflare R2에서 파일을 삭제한다. */
    @Override
    public void delete(String fileUrl) { ... }
}
```

---

### 2-4. 인터페이스 분리 원칙 (ISP - Interface Segregation Principle)

> 클라이언트는 자신이 사용하지 않는 메서드에 의존하지 않아야 한다.

**적용 방법**
- 하나의 거대한 인터페이스 대신 역할별로 인터페이스를 분리
- 청첩장 조회용 / 편집용 인터페이스를 필요에 따라 분리

```java
// ✅ 올바른 예 - 역할별로 인터페이스 분리
/** 청첩장 조회 전용 인터페이스 (하객용) */
public interface McardQueryService {
    McardResponseDto getMcardByInviteCode(String inviteCode);
}

/** 청첩장 편집 전용 인터페이스 (제작자용) */
public interface McardCommandService {
    McardResponseDto createMcard(McardCreateRequestDto dto, Long userId);
    McardResponseDto updateMcard(Long mcardId, McardUpdateRequestDto dto);
    void deleteMcard(Long mcardId);
}

// ❌ 잘못된 예 - 모든 기능을 하나의 인터페이스에 몰아넣음
public interface McardService {
    McardResponseDto getMcardByInviteCode(String inviteCode); // 하객용
    McardResponseDto createMcard(...);                         // 제작자용
    void deleteMcard(Long mcardId);                            // 제작자용
    // ... 계속 추가되면 비대해짐
}
```

---

### 2-5. 의존성 역전 원칙 (DIP - Dependency Inversion Principle)

> 고수준 모듈은 저수준 모듈에 의존하지 않는다. 둘 다 추상화에 의존해야 한다.

**적용 방법**
- 구현체가 아닌 인터페이스에 의존하도록 설계
- Spring의 `@Autowired` / 생성자 주입으로 의존성 주입 (DI 컨테이너 활용)
- 의존성 주입은 **생성자 주입** 방식만 사용 (`@RequiredArgsConstructor`)

```java
// ✅ 올바른 예 - 인터페이스에 의존 (DIP 준수)
@Service
@RequiredArgsConstructor
public class GalleryServiceImpl implements GalleryService {
    private final FileStorage fileStorage;       // 인터페이스에 의존
    private final GalleryRepository galleryRepository;
}

// ❌ 잘못된 예 - 구현체에 직접 의존
@Service
public class GalleryServiceImpl {
    private final CloudflareR2FileStorage r2FileStorage;   // 구현체에 직접 의존 ❌
}
```

---

## 3. 주석 작성 규칙

모든 클래스, 메서드, 복잡한 로직에는 **JavaDoc 형식 주석**을 상세하게 작성한다.

---

### 3-1. 클래스 주석

모든 클래스 상단에 역할, 담당 도메인, 주요 협력 클래스를 명시한다.

```java
/**
 * 청첩장 갤러리 서비스 구현체
 *
 * <p>청첩장에 포함되는 사진 갤러리의 업로드, 순서 변경, 삭제 등
 * 갤러리 관련 비즈니스 로직을 처리한다.</p>
 *
 * @see GalleryService
 * @see GalleryRepository
 * @see FileStorage
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GalleryServiceImpl implements GalleryService {
    ...
}
```

---

### 3-2. 메서드 주석

모든 public 메서드에 기능 설명, 파라미터, 반환값, 예외를 명시한다.

```java
/**
 * 갤러리 사진 업로드
 *
 * <p>업로드된 이미지를 Cloudflare R2에 저장하고, 해당 청첩장의 갤러리에 사진 정보를 등록한다.
 * 업로드 순서는 현재 마지막 순서 다음으로 자동 설정된다.</p>
 *
 * @param mcardId 사진을 추가할 청첩장 ID
 * @param file    업로드할 이미지 파일 (jpg, png, webp 허용)
 * @return 저장된 사진 정보 DTO (photoId, url, order 포함)
 * @throws EntityNotFoundException 청첩장이 존재하지 않을 경우
 * @throws FileUploadException     파일 업로드 실패 시
 */
@Override
@Transactional
public GalleryPhotoResponseDto uploadPhoto(Long mcardId, MultipartFile file) {
    ...
}
```

---

### 3-3. 로직 내 인라인 주석

복잡한 처리 흐름, 조건 분기, 외부 API 호출 등에는 단계별로 인라인 주석을 작성한다.

```java
@Override
@Transactional
public GalleryPhotoResponseDto uploadPhoto(Long mcardId, MultipartFile file) {

    // 1. 청첩장 존재 여부 확인
    Mcard mcard = mcardRepository.findById(mcardId)
        .orElseThrow(() -> new EntityNotFoundException("청첩장을 찾을 수 없습니다. id=" + mcardId));

    // 2. Cloudflare R2에 이미지 업로드 후 접근 URL 획득
    String imageUrl = fileStorage.upload(file);

    // 3. 현재 갤러리의 마지막 순서 조회 (순서 자동 부여)
    int nextOrder = galleryRepository.findMaxOrderByMcardId(mcardId)
        .map(order -> order + 1)
        .orElse(1); // 사진이 없으면 순서 1부터 시작

    // 4. 갤러리 사진 엔티티 생성 및 저장
    GalleryPhoto photo = GalleryPhoto.builder()
        .mcard(mcard)
        .imageUrl(imageUrl)
        .displayOrder(nextOrder)
        .build();
    GalleryPhoto savedPhoto = galleryRepository.save(photo);

    // 5. 저장된 사진 정보를 DTO로 변환하여 반환
    return GalleryPhotoResponseDto.from(savedPhoto);
}
```

---

### 3-4. 상수 및 필드 주석

의미가 불분명한 상수, 설정값, 필드에는 반드시 설명을 달아준다.

```java
/** 청첩장 공개 URL에 사용되는 초대 코드 길이 */
private static final int INVITE_CODE_LENGTH = 8;

/** 결제 완료 후 청첩장 유효 기간 (예식일 기준 +30일) */
private static final int MCARD_EXPIRY_DAYS_AFTER_WEDDING = 30;

/** 갤러리 최대 업로드 사진 수 */
private static final int GALLERY_MAX_PHOTO_COUNT = 50;
```

---

## 4. 기타 공통 규칙

### 4-1. 패키지 구조
```
com.project
├── controller      # Controller 계층
├── service         # Service 인터페이스
│   └── impl        # Service 구현체
├── repository      # Repository 계층
├── domain          # Entity 클래스
├── dto
│   ├── request     # 요청 DTO
│   └── response    # 응답 DTO
├── exception       # 커스텀 예외 클래스
├── config          # 설정 클래스 (Security, JPA 등)
└── util            # 공통 유틸리티
```

### 4-2. 예외 처리
- 커스텀 예외 클래스를 도메인별로 정의하여 사용
- `@RestControllerAdvice`로 전역 예외 핸들러 구현
- 예외 발생 시 원인을 주석 또는 메시지로 명확히 명시

### 4-3. DTO 규칙
- 요청 DTO: `{기능}RequestDto` (예: `McardCreateRequestDto`)
- 응답 DTO: `{기능}ResponseDto` (예: `McardResponseDto`)
- Entity를 Controller/Service 간 직접 전달 금지 — 반드시 DTO 사용
- DTO 내 `from(Entity)` 정적 팩토리 메서드로 변환 처리

### 4-4. 키워드 통일 원칙

> 동일한 의미를 가진 단어는 코드베이스 전체에서 하나의 표현으로 통일한다.

**규칙**
- 같은 개념을 나타내는 변수명·필드명·파라미터명이 두 가지 이상 혼재하는 것을 금지한다.
- 새로운 키워드를 도입하기 전에, 이미 프로젝트에서 사용 중인 단어가 있는지 먼저 확인한다.
- 표준 약어가 존재하는 경우 풀네임 대신 약어를 사용한다.

**프로젝트 적용 키워드 표**

| 개념 | 사용 ✅ | 금지 ❌ |
|------|--------|--------|
| 위도 | `lat` | `latitude` |
| 경도 | `lng` | `longitude`, `lon` |
| 청첩장 ID | `mcardId` | `cardId`, `invitationId` |
| 예식장 ID | `venueId` | `locationId`, `placeId` |
| 교통수단 순서 | `displayOrder` | `order`, `sortOrder`, `seq` |

> 새 약어/키워드가 확정되면 이 표에 반드시 추가한다.

---

### 4-5. 네이밍 컨벤션

| 대상 | 규칙 | 예시 |
|------|------|------|
| 클래스 | PascalCase | `McardService`, `GalleryController` |
| 메서드/변수 | camelCase | `getMcard()`, `inviteCode` |
| 상수 | UPPER_SNAKE_CASE | `INVITE_CODE_LENGTH` |
| 테이블/컬럼 | snake_case | `mcard_id`, `invite_code` |
| API 경로 | kebab-case | `/api/v1/mcards`, `/photo-quote` |

### 4-6. API 응답 형식 규칙

모든 API는 일관된 응답 형식을 사용한다. 성공/실패 여부와 데이터를 명확히 구분하여 반환한다.

**응답 형식**
```json
{
  "code": 200,
  "message": "Success",
  "datas": {
    // 실제 데이터
  }
}
```

**규칙**
- `code`: HTTP 상태 코드 (200, 400, 401, 403, 404, 500 등)
- `message`: 응답 메시지 (enum으로 정의하여 일관성 유지)
- `datas`: 실제 응답 데이터 (객체 또는 배열)
- 모든 Controller는 `ApiResponse<T>` 래퍼 클래스를 사용하여 반환
- 예외 발생 시에도 동일한 형식으로 에러 응답 반환

**예시**
```java
/**
 * 청첩장 단건 조회
 * @param mcardId 조회할 청첩장 ID
 * @return 청첩장 상세 데이터
 */
@GetMapping("/{mcardId}")
public ApiResponse<McardResponseDto> getMcard(@PathVariable Long mcardId) {
    McardResponseDto data = mcardService.getMcard(mcardId);
    return ApiResponse.success(data);
}
```
