# API Response Examples

> 기준: Spring Boot 백엔드 소스코드 + `mcardId=5` 데이터 기반  
> Base URL: `http://localhost:8080/api/v1`  
> 인증: `Authorization: Bearer {accessToken}` (하객용·정적 목록 API 제외)

---

## 공통 응답 형식

### 성공 응답
```json
{
  "code": 200,
  "message": "Success",
  "datas": { ... }
}
```

### 실패 응답
```json
{
  "code": 400 | 401 | 403 | 404 | 500,
  "message": "에러 설명",
  "datas": null
}
```

---

## 1. 헬스 체크

### `GET /health`
> 인증 불필요

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": {
    "status": "UP",
    "timestamp": "2026-05-05T10:30:00.123",
    "service": "wedding-invitation-backend",
    "version": "0.0.1",
    "serverIp": "172.17.0.3",
    "hostname": "3f8a2b1c9d04"
  }
}
```

---

## 2. 인증 (Auth)

### `GET /api/v1/auth/test-login/{userId}` [개발 전용]
> 인증 불필요

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": {
    "userId": 1,
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsImlhdCI6MTc0NjQ0MDAwMCwiZXhwIjoxNzQ3MDQ0ODAwfQ.xXxXxXxXxX"
  }
}
```

**실패 - 존재하지 않는 userId (404)**
```json
{
  "code": 404,
  "message": "사용자를 찾을 수 없습니다. id=999",
  "datas": null
}
```

---

### `GET /api/v1/auth/me`
> JWT 필요

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": {
    "userId": 1,
    "name": "홍길동",
    "email": "hong@example.com",
    "profileImageUrl": "https://k.kakaocdn.net/dn/example/img.jpg"
  }
}
```

**실패 - 토큰 없음 (401)**
```json
{
  "code": 401,
  "message": "인증이 필요합니다.",
  "datas": null
}
```

**실패 - 토큰 만료 (401)**
```json
{
  "code": 401,
  "message": "만료된 토큰입니다.",
  "datas": null
}
```

---

## 3. 청첩장 (MCard)

### `GET /api/v1/mcards`
> 내 청첩장 목록 조회

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": [
    {
      "mcardId": 5,
      "title": "홍길동 ♥ 김영희 결혼합니다",
      "inviteCode": "ab12cd34",
      "hasWatermark": true,
      "weddingDateTime": "2026-06-14T11:00:00",
      "createdAt": "2026-05-01T09:00:00",
      "updatedAt": "2026-05-05T10:00:00"
    }
  ]
}
```

---

### `POST /api/v1/mcards`
> 청첩장 신규 생성

**요청**
```json
{
  "title": "홍길동 ♥ 김영희 결혼합니다"
}
```

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": {
    "mcardId": 5,
    "title": "홍길동 ♥ 김영희 결혼합니다",
    "inviteCode": "ab12cd34",
    "hasWatermark": true,
    "weddingDateTime": null,
    "createdAt": "2026-05-05T10:00:00",
    "updatedAt": "2026-05-05T10:00:00"
  }
}
```

---

### `GET /api/v1/mcards/{mcardId}`
> 청첩장 단건 조회 (편집용)

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": {
    "mcardId": 5,
    "title": "홍길동 ♥ 김영희 결혼합니다",
    "inviteCode": "ab12cd34",
    "hasWatermark": true,
    "weddingDateTime": "2026-06-14T11:00:00",
    "createdAt": "2026-05-01T09:00:00",
    "updatedAt": "2026-05-05T10:00:00"
  }
}
```

**실패 - 존재하지 않음 (404)**
```json
{
  "code": 404,
  "message": "청첩장을 찾을 수 없습니다. id=999",
  "datas": null
}
```

**실패 - 타인의 청첩장 (403)**
```json
{
  "code": 403,
  "message": "접근 권한이 없습니다.",
  "datas": null
}
```

---

### `DELETE /api/v1/mcards/{mcardId}`
> 청첩장 삭제 (soft delete)

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": null
}
```

---

## 4. 하객 공개 뷰

### `GET /api/v1/w/{inviteCode}`
> 인증 불필요

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": {
    "mcardId": 13,
    "title": "홍길동 ♥ 김영희 결혼합니다",
    "inviteCode": "ab12cd34",
    "hasWatermark": true,
    "createdAt": "2026-05-04T21:13:17.13428",
    "updatedAt": "2026-05-04T21:13:17.13428",
    "couple": {
      "coupleId": 5,
      "mcardId": 5,
      "groomName": "홍길동",
      "brideName": "김영희",
      "groomFatherName": "홍판서",
      "groomMotherName": "춘향어머니",
      "brideFatherName": "김부장",
      "brideMotherName": "이순이",
      "groomFatherDeceased": false,
      "groomMotherDeceased": null,
      "brideFatherDeceased": null,
      "brideMotherDeceased": null,
      "showGroomContacts": null,
      "showBrideContacts": null
    },
    "greeting": {
      "greetingId": 5,
      "mcardId": 5,
      "title": "저희 결혼합니다",
      "content": "서로 다른 두 사람이 만나\n하나가 되려 합니다.",
      "imageUrl": null,
      "fontSize": "medium"
    },
    "schedule": {
      "scheduleId": 5,
      "mcardId": 5,
      "weddingDateTime": "2026-06-14T11:00:00",
      "prepTimeMinutes": 30,
      "showCalendar": true
    },
    "venue": {
      "venueId": 5,
      "mcardId": 5,
      "venueName": "더케이호텔 서울",
      "hallName": "2층 가야금홀",
      "address": "서울 서초구 헌릉로 177",
      "lat": 37.4620,
      "lng": 127.0403,
      "mapImageUrl": "https://pub-xxx.r2.dev/maps/mcard5-map.png",
      "showMap": true,
      "mapLocked": false,
      "showTransportIcons": true,
      "transports": [
        {
          "transportId": 1,
          "type": "subway",
          "description": "3호선 양재역 2번 출구 도보 5분",
          "displayOrder": 1
        }
      ]
    },
    "gallery": [
      "https://pub-xxx.r2.dev/gallery/photo1.jpg",
      "https://pub-xxx.r2.dev/gallery/photo2.jpg"
    ],
    "accounts": [
      {
        "accountId": 1,
        "mcardId": 5,
        "side": "groom",
        "bankName": "카카오뱅크",
        "accountNumber": "3333-12-3456789",
        "accountHolder": "홍길동",
        "displayOrder": 1
      }
    ],
    "contacts": [
      {
        "contactId": 1,
        "mcardId": 5,
        "contactType": "groom",
        "name": "홍길동",
        "phoneNumber": "010-1234-5678",
        "isVisible": true,
        "relation": "groom",
        "role": "groom"
      }
    ],
    "quote": {
      "quoteId": 5,
      "mcardId": 5,
      "quoteContent": "사랑은 두 사람이 같은 방향을 바라보는 것이다.",
      "fontSize": "medium"
    },
    "video": {
      "videoId": 5,
      "mcardId": 5,
      "videoUrl": "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
      "videoTitle": "우리의 이야기"
    },
    "wreath": null,
    "rsvpSettings": null,
    "guestbookSettings": null,
    "guestbookMessages": [
      {
        "messageId": 3,
        "mcardId": 5,
        "guestName": "이하객",
        "content": "축하드립니다!",
        "isSecret": false,
        "createdAt": "2026-05-04T15:30:00"
      }
    ]
  }
}
```

> **실제 응답 기준 주의사항**
> - `gallery`: 객체 배열이 아닌 **URL 문자열 배열** (`string[]`)
> - `venue.transports[].type`: `transportType`이 아닌 **`type`** 필드명 사용
> - `venue`: `latitude/longitude` 아닌 **`lat/lng`** 필드명 사용
> - `rsvpSettings`, `guestbookSettings`: 미설정 시 **`null`** 반환 (객체 보장 아님)
> - `couple.*Deceased`, `couple.show*Contacts`: **`null`** 가능

**실패 - 잘못된 초대 코드 (404)**
```json
{
  "code": 404,
  "message": "청첩장을 찾을 수 없습니다. inviteCode=xxxxxxxx",
  "datas": null
}
```

---

## 5. 테마

### `GET /api/v1/mcards/{mcardId}/theme`

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": {
    "themeId": 5,
    "mcardId": 5,
    "themeStyle": "classic",
    "color": "beige",
    "fontFamily": "나눔명조",
    "fontWeight": "normal",
    "preventZoom": false,
    "enableScrollAnimation": true
  }
}
```

### `PUT /api/v1/mcards/{mcardId}/theme`

**요청**
```json
{
  "themeStyle": "classic",
  "color": "beige",
  "fontFamily": "나눔명조",
  "fontWeight": "normal",
  "preventZoom": false,
  "enableScrollAnimation": true
}
```

**성공 (200)** — 응답 형태는 GET과 동일

---

## 6. 인트로

### `GET /api/v1/mcards/{mcardId}/intro`

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": {
    "introId": 5,
    "mcardId": 5,
    "introStyleKey": "OVAL",
    "createdAt": "2026-05-01T09:00:00",
    "updatedAt": "2026-05-05T10:00:00"
  }
}
```

### `PUT /api/v1/mcards/{mcardId}/intro`

**요청**
```json
{
  "introStyleKey": "OVAL"
}
```

**성공 (200)** — 응답 형태는 GET과 동일

---

## 7. 신랑·신부 정보

### `GET /api/v1/mcards/{mcardId}/couple`

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": {
    "coupleId": 5,
    "mcardId": 5,
    "groomName": "홍길동",
    "brideName": "김영희",
    "groomFatherName": "홍판서",
    "groomMotherName": "춘향어머니",
    "brideFatherName": "김부장",
    "brideMotherName": "이순이",
    "groomFatherDeceased": false,
    "groomMotherDeceased": false,
    "brideFatherDeceased": false,
    "brideMotherDeceased": false,
    "showGroomContacts": true,
    "showBrideContacts": true,
    "createdAt": "2026-05-01T09:00:00",
    "updatedAt": "2026-05-05T10:00:00"
  }
}
```

### `PUT /api/v1/mcards/{mcardId}/couple`

**요청**
```json
{
  "groomName": "홍길동",
  "brideName": "김영희",
  "groomFatherName": "홍판서",
  "groomMotherName": "춘향어머니",
  "brideFatherName": "김부장",
  "brideMotherName": "이순이",
  "groomFatherDeceased": false,
  "groomMotherDeceased": false,
  "brideFatherDeceased": false,
  "brideMotherDeceased": false,
  "showGroomContacts": true,
  "showBrideContacts": true
}
```

**성공 (200)** — 응답 형태는 GET과 동일

---

## 8. 모시는 글 (인사말)

### `GET /api/v1/mcards/{mcardId}/greeting`

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": {
    "greetingId": 5,
    "mcardId": 5,
    "title": "저희 결혼합니다",
    "content": "서로 다른 두 사람이 만나\n하나가 되려 합니다.\n\n소중한 분들을 모시고\n작은 예식을 올리고자 합니다.",
    "imageUrl": null,
    "fontSize": "medium"
  }
}
```

### `GET /api/v1/greetings/samples`
> 인증 불필요

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": [
    {
      "id": 1,
      "text": "서로 다른 두 사람이 만나\n하나가 되려 합니다..."
    },
    {
      "id": 2,
      "text": "두 사람이 사랑으로 하나가 되는 날..."
    }
  ]
}
```

---

## 9. 예식 일시

### `GET /api/v1/mcards/{mcardId}/schedule`

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": {
    "scheduleId": 5,
    "mcardId": 5,
    "weddingDateTime": "2026-06-14T11:00:00",
    "prepTimeMinutes": 30,
    "createdAt": "2026-05-01T09:00:00",
    "updatedAt": "2026-05-05T10:00:00"
  }
}
```

### `PUT /api/v1/mcards/{mcardId}/schedule`

**요청**
```json
{
  "weddingDateTime": "2026-06-14T11:00:00",
  "prepTimeMinutes": 30
}
```

---

## 10. 예식 장소

### `GET /api/v1/mcards/{mcardId}/venue`

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": {
    "venueId": 5,
    "mcardId": 5,
    "venueName": "더케이호텔 서울",
    "floorInfo": "2층 가야금홀",
    "hallName": "2층 가야금홀",
    "address": "서울 서초구 헌릉로 177",
    "latitude": 37.4620,
    "longitude": 127.0403,
    "lat": 37.4620,
    "lng": 127.0403,
    "mapImageUrl": "https://pub-xxx.r2.dev/maps/mcard5-map.png",
    "showMap": true,
    "mapLocked": false,
    "showTransportIcons": true,
    "transports": [
      {
        "transportId": 1,
        "transportType": "subway",
        "description": "3호선 양재역 2번 출구 도보 5분",
        "displayOrder": 1
      },
      {
        "transportId": 2,
        "transportType": "bus",
        "description": "양재역 정류장 하차 후 도보 3분",
        "displayOrder": 2
      }
    ]
  }
}
```

### `PUT /api/v1/mcards/{mcardId}/venue`

**요청**
```json
{
  "venueName": "더케이호텔 서울",
  "hallName": "2층 가야금홀",
  "address": "서울 서초구 헌릉로 177",
  "lat": 37.4620,
  "lng": 127.0403,
  "showMap": true,
  "lockMap": false,
  "showTransportIcons": true
}
```

> `mapImageUrl`은 서버가 lat/lng로 네이버 Static Map API를 호출해 자동 생성하므로 요청에 포함하지 않는다.

---

### `POST /api/v1/mcards/{mcardId}/venue/transports`

**요청**
```json
{
  "transportType": "subway",
  "description": "3호선 양재역 2번 출구 도보 5분",
  "displayOrder": 1
}
```

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": {
    "transportId": 1,
    "transportType": "subway",
    "description": "3호선 양재역 2번 출구 도보 5분",
    "displayOrder": 1
  }
}
```

### `DELETE /api/v1/mcards/{mcardId}/venue/transports/{transportId}`

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": null
}
```

**실패 - 존재하지 않음 (404)**
```json
{
  "code": 404,
  "message": "교통수단 정보를 찾을 수 없습니다. id=999",
  "datas": null
}
```

---

## 11. 주소 검색 / 지도 이미지

### `GET /api/v1/address/search?query={키워드}`
> JWT 필요

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": {
    "addresses": [
      {
        "placeName": "더케이호텔 서울",
        "addressName": "서울 서초구 헌릉로 177",
        "roadAddress": "서울 서초구 헌릉로 177",
        "jibunAddress": "서울 서초구 내곡동 1-68",
        "latitude": 37.4620,
        "longitude": 127.0403
      }
    ]
  }
}
```

### `GET /api/v1/address/map?lat={위도}&lng={경도}&width={px}&height={px}`
> JWT 필요

**성공 (200)** — `Content-Type: image/png`, body는 PNG 바이너리 직접 반환

**실패 - 파라미터 누락 (400)**
```json
{
  "code": 400,
  "message": "lat, lng 파라미터는 필수입니다.",
  "datas": null
}
```

---

## 12. 갤러리

### `GET /api/v1/mcards/{mcardId}/gallery`

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": [
    {
      "photoId": 10,
      "mcardId": 5,
      "imageUrl": "https://pub-xxx.r2.dev/gallery/photo1.jpg",
      "publicUrl": "https://pub-xxx.r2.dev/gallery/photo1.jpg",
      "displayOrder": 1,
      "layoutType": "grid"
    },
    {
      "photoId": 11,
      "mcardId": 5,
      "imageUrl": "https://pub-xxx.r2.dev/gallery/photo2.jpg",
      "publicUrl": "https://pub-xxx.r2.dev/gallery/photo2.jpg",
      "displayOrder": 2,
      "layoutType": "grid"
    }
  ]
}
```

### `POST /api/v1/mcards/{mcardId}/gallery`
> `multipart/form-data`, field name: `file`

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": {
    "photoId": 12,
    "mcardId": 5,
    "imageUrl": "https://pub-xxx.r2.dev/gallery/photo3.jpg",
    "publicUrl": "https://pub-xxx.r2.dev/gallery/photo3.jpg",
    "displayOrder": 3,
    "layoutType": "grid"
  }
}
```

### `PUT /api/v1/mcards/{mcardId}/gallery/order`

**요청**
```json
{
  "photoIds": [11, 10, 12]
}
```
> `photoIds` 배열의 순서가 곧 새 `displayOrder`가 됩니다 (첫 번째 = 1, 두 번째 = 2, …).

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": [
    { "photoId": 11, "mcardId": 5, "imageUrl": "https://pub-xxx.r2.dev/gallery/photo2.jpg", "publicUrl": "https://pub-xxx.r2.dev/gallery/photo2.jpg", "displayOrder": 1, "layoutType": "grid" },
    { "photoId": 10, "mcardId": 5, "imageUrl": "https://pub-xxx.r2.dev/gallery/photo1.jpg", "publicUrl": "https://pub-xxx.r2.dev/gallery/photo1.jpg", "displayOrder": 2, "layoutType": "grid" },
    { "photoId": 12, "mcardId": 5, "imageUrl": "https://pub-xxx.r2.dev/gallery/photo3.jpg", "publicUrl": "https://pub-xxx.r2.dev/gallery/photo3.jpg", "displayOrder": 3, "layoutType": "grid" }
  ]
}
```

### `DELETE /api/v1/mcards/{mcardId}/gallery/{photoId}`

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": null
}
```

---

## 13. 연락하기

### `GET /api/v1/mcards/{mcardId}/contacts`

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": [
    {
      "contactId": 1,
      "mcardId": 5,
      "contactType": "groom",
      "name": "홍길동",
      "phoneNumber": "010-1234-5678",
      "isVisible": true
    },
    {
      "contactId": 2,
      "mcardId": 5,
      "contactType": "bride",
      "name": "김영희",
      "phoneNumber": "010-9876-5432",
      "isVisible": true
    },
    {
      "contactId": 3,
      "mcardId": 5,
      "contactType": "groom_father",
      "name": "홍판서",
      "phoneNumber": "010-1111-2222",
      "isVisible": true
    }
  ]
}
```

---

## 14. 계좌번호

### `GET /api/v1/mcards/{mcardId}/accounts`

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": [
    {
      "accountId": 1,
      "mcardId": 5,
      "side": "groom",
      "bankName": "카카오뱅크",
      "accountNumber": "3333-12-3456789",
      "accountHolder": "홍길동",
      "displayOrder": 1
    },
    {
      "accountId": 2,
      "mcardId": 5,
      "side": "bride",
      "bankName": "신한은행",
      "accountNumber": "110-123-456789",
      "accountHolder": "김영희",
      "displayOrder": 2
    }
  ]
}
```

> `side` 필드는 소스코드상 `accountType`이나 `@JsonProperty("side")`로 직렬화됨

### `POST /api/v1/mcards/{mcardId}/accounts`

**요청**
```json
{
  "accountType": "groom",
  "bankName": "카카오뱅크",
  "accountNumber": "3333-12-3456789",
  "accountHolder": "홍길동",
  "displayOrder": 1
}
```

### `DELETE /api/v1/mcards/{mcardId}/accounts/{accountId}`

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": null
}
```

---

## 15. 배경음악

### `GET /api/v1/mcards/{mcardId}/bgm`

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": {
    "bgmId": 5,
    "mcardId": 5,
    "bgmUrl": "https://pub-xxx.r2.dev/bgm/wedding-song.mp3",
    "publicUrl": "https://pub-xxx.r2.dev/bgm/wedding-song.mp3",
    "bgmTitle": "A Thousand Years",
    "autoPlay": false
  }
}
```

### `POST /api/v1/mcards/{mcardId}/bgm/upload`
> `multipart/form-data`, field name: `file`

**성공 (200)** — 응답 형태는 GET과 동일

---

## 16. 안내사항

### `GET /api/v1/mcards/{mcardId}/notices`

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": [
    {
      "noticeId": 1,
      "mcardId": 5,
      "title": "주차 안내",
      "content": "호텔 지하 주차장을 이용해 주세요.\n주차 확인증은 행사장 입구에서 받으실 수 있습니다.",
      "imageUrl": null,
      "displayOrder": 1
    }
  ]
}
```

### `POST /api/v1/mcards/{mcardId}/notices`

**요청**
```json
{
  "title": "주차 안내",
  "content": "호텔 지하 주차장을 이용해 주세요.",
  "imageUrl": null,
  "displayOrder": 1
}
```

### `DELETE /api/v1/mcards/{mcardId}/notices/{noticeId}`

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": null
}
```

---

## 17. 참석의사 (RSVP)

### `GET /api/v1/mcards/{mcardId}/rsvp/settings`

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": {
    "rsvpSettingId": 5,
    "mcardId": 5,
    "isEnabled": true
  }
}
```

### `POST /api/v1/mcards/{mcardId}/rsvp`
> 인증 불필요 (하객용)

**요청**
```json
{
  "responderName": "이하객",
  "responderPhone": "010-5555-6666",
  "willAttend": true,
  "attendeeCount": 2,
  "message": "축하드립니다! 꼭 참석하겠습니다."
}
```

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": {
    "responseId": 1,
    "mcardId": 5,
    "responderName": "이하객",
    "responderPhone": "010-5555-6666",
    "willAttend": true,
    "attendeeCount": 2,
    "message": "축하드립니다! 꼭 참석하겠습니다.",
    "respondedAt": "2026-05-05T10:30:00"
  }
}
```

**실패 - RSVP 비활성 (400)**
```json
{
  "code": 400,
  "message": "RSVP가 비활성화된 청첩장입니다.",
  "datas": null
}
```

### `GET /api/v1/mcards/{mcardId}/rsvp`
> 제작자 전용, JWT 필요

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": [
    {
      "responseId": 1,
      "mcardId": 5,
      "responderName": "이하객",
      "responderPhone": "010-5555-6666",
      "willAttend": true,
      "attendeeCount": 2,
      "message": "꼭 참석하겠습니다.",
      "respondedAt": "2026-05-05T10:30:00"
    }
  ]
}
```

---

## 18. 방명록

### `GET /api/v1/mcards/{mcardId}/guestbook`
> 인증 불필요

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": [
    {
      "messageId": 3,
      "mcardId": 5,
      "guestName": "이하객",
      "content": "결혼 축하드립니다! 행복하게 사세요 :)",
      "isSecret": false,
      "createdAt": "2026-05-04T15:30:00"
    }
  ]
}
```

### `POST /api/v1/mcards/{mcardId}/guestbook`
> 인증 불필요 (하객용)

**요청**
```json
{
  "guestName": "이하객",
  "content": "결혼 축하드립니다!",
  "isSecret": false
}
```

**성공 (200)** — 응답 형태는 GET 목록의 단건과 동일

**실패 - 방명록 비활성 (400)**
```json
{
  "code": 400,
  "message": "방명록이 비활성화된 청첩장입니다.",
  "datas": null
}
```

### `DELETE /api/v1/mcards/{mcardId}/guestbook/{messageId}`
> JWT 필요

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": null
}
```

---

## 19. 글귀

### `GET /api/v1/mcards/{mcardId}/quote`

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": {
    "quoteId": 5,
    "mcardId": 5,
    "quoteContent": "사랑은 두 사람이 서로를\n바라보는 것이 아니라\n함께 같은 방향을 바라보는 것이다.",
    "fontSize": "medium"
  }
}
```

---

## 20. 사진 & 글귀

### `GET /api/v1/mcards/{mcardId}/photo-quote`

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": {
    "photoQuoteId": 5,
    "mcardId": 5,
    "imageUrl": "https://pub-xxx.r2.dev/photo-quote/couple.jpg",
    "quoteText": "함께라서 더 행복합니다"
  }
}
```

---

## 21. 동영상

### `GET /api/v1/mcards/{mcardId}/video`

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": {
    "videoId": 5,
    "mcardId": 5,
    "videoUrl": "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
    "videoTitle": "우리의 이야기"
  }
}
```

---

## 22. 화환 보내기

### `GET /api/v1/mcards/{mcardId}/wreath`

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": {
    "wreathId": 5,
    "mcardId": 5,
    "wreathUrl": "https://www.floristry.co.kr/product/wedding"
  }
}
```

---

## 23. 공유 썸네일

### `GET /api/v1/mcards/{mcardId}/thumbnail`

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": {
    "thumbnailId": 5,
    "mcardId": 5,
    "kakaotalkThumbnailUrl": "https://pub-xxx.r2.dev/thumbnail/kakao-thumb.jpg",
    "urlShareThumbnailUrl": "https://pub-xxx.r2.dev/thumbnail/url-thumb.jpg"
  }
}
```

---

## 24. 메뉴 순서

### `GET /api/v1/mcards/{mcardId}/sections/order`

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": {
    "sectionOrderId": 5,
    "mcardId": 5,
    "sectionOrder": [
      "greeting",
      "gallery",
      "schedule",
      "venue",
      "contacts",
      "accounts",
      "video",
      "bgm",
      "notices",
      "rsvp",
      "guestbook",
      "wreath",
      "quote",
      "photo-quote"
    ]
  }
}
```

### `PUT /api/v1/mcards/{mcardId}/sections/order`

**요청**
```json
{
  "sectionOrder": ["greeting", "gallery", "schedule", "venue", "contacts", "accounts"]
}
```

---

## 25. QR 코드

### `GET /api/v1/mcards/{mcardId}/qrcode`

**성공 (200)** — `Content-Type: image/png`, `Content-Disposition: attachment; filename="qrcode.png"`, body는 PNG 바이너리

**실패 - 존재하지 않음 (404)**
```json
{
  "code": 404,
  "message": "청첩장을 찾을 수 없습니다. id=999",
  "datas": null
}
```

---

## 26. 파일 업로드 (공통)

### `POST /api/v1/files/upload?folder={folder}`
> `multipart/form-data`, field name: `file`
> folder: `gallery` | `bgm` | `photo-quote` | `thumbnail` | `test`

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": {
    "fileUrl": "https://pub-xxx.r2.dev/gallery/uuid-filename.jpg"
  }
}
```

**실패 - 파일 없음 (400)**
```json
{
  "code": 400,
  "message": "파일이 비어있습니다.",
  "datas": null
}
```

**실패 - R2 업로드 실패 (400)**
```json
{
  "code": 400,
  "message": "파일 업로드에 실패했습니다.",
  "datas": null
}
```

### `DELETE /api/v1/files?fileUrl={fileUrl}`

**성공 (200)**
```json
{
  "code": 200,
  "message": "Success",
  "datas": null
}
```

---

## 공통 에러 코드 정리

| HTTP Status | code | 발생 상황 |
|-------------|------|-----------|
| 200 | 200 | 성공 |
| 400 | 400 | 잘못된 요청, 유효성 검증 실패, 파일 업로드 실패 |
| 401 | 401 | JWT 없음, 만료, 서명 불일치 |
| 403 | 403 | 타인의 청첩장 접근 시도 |
| 404 | 404 | 리소스를 찾을 수 없음 |
| 500 | 500 | 서버 내부 오류 |

---

## 유효성 검증 실패 예시

**요청 필드에 `@Valid` 위반 시 (400)**
```json
{
  "code": 400,
  "message": "responderName: 이름은 필수입니다., willAttend: 참석 여부는 필수입니다.",
  "datas": null
}
```
