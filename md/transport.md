# 교통수단 API 고도화 계획

## 목표 UI

```
[지하철]
[분당선] 서울숲역 5번 출구 도보 2분
[2호선] 뚝섬역 8번 출구 도보 5분

[버스]
뚝섬 서울숲 정류장 / 121, 141, 145, 148, 463
뚝섬역 8번 출구 정류장 / 2016, 2224, 2413

[주차안내]
건물 내 B3-B7 2시간 무료주차
안내 데스크에서 주차권 수령
```

---

## 고도화 순서

### 1단계. 지하철 API (`GET /api/v1/transports/subways`)

**현재 응답**
```json
{
  "name": "학동역 7호선",
  "distance": 353,
  "address": "서울 강남구 학동로 지하 180"
}
```

**목표 응답**
```json
{
  "lineName": "7호선",
  "stationName": "학동역",
  "exitNumber": "3번 출구",
  "walkingMinutes": 6
}
```

**작업 내용**
- `lineName`: 현재 `place_name`에서 파싱 가능 ("학동역 7호선" → "7호선")
- `stationName`: 현재 `place_name`에서 파싱 가능 ("학동역 7호선" → "학동역")
- `exitNumber`: 카카오 SW8은 출구 단위 데이터 미제공 → **[미정] 출구 번호를 데이터로 제공할 방법 검토 필요** (후보: 공공데이터 지하철 출구 좌표 DB 적재 후 좌표 기반 최근접 출구 쿼리)
- `walkingMinutes`: `distance ÷ 67m/분`으로 근사값 계산 (올림 처리)
- Response DTO 변경 (`TransportItem` → `SubwayItem`)
- `TransportSuggestionsResponseDto.fromKakaoResponse()` 파싱 로직 수정

---

### 2단계. 버스 API (`GET /api/v1/transports/buses`)

**현재 응답**
```json
[]
```

**목표 응답**
```json
[
  {
    "stationName": "뚝섬 서울숲 정류장",
    "routes": ["121", "141", "145", "148", "463"]
  },
  {
    "stationName": "뚝섬역 8번 출구 정류장",
    "routes": ["2016", "2224", "2413"]
  }
]
```

**작업 내용**
- 사용 API: 공공데이터포털 TAGO API 또는 서울 열린데이터광장 버스 API
- 좌표 기반 근처 정류장 조회 → 정류장명 추출
- 정류장 ID 기반 경유 노선번호 목록 조회
- `BusClient` 신규 생성
- `BusItem` Response DTO 신규 생성
- `TransportServiceImpl.getBuses()` 구현

---

### 3단계. 주차 API (`GET /api/v1/transports/parkings`)

**현재 응답**
```json
{
  "name": "강남구청 공영주차장",
  "distance": 150,
  "address": "서울 강남구 삼성동 1"
}
```

**목표 응답**
```json
{
  "name": "건물 내 B3-B7",
  "description": "2시간 무료주차",
  "tip": "안내 데스크에서 주차권 수령"
}
```

**작업 내용**
- 건물 내부 주차 정보(층수, 무료 시간, 주차권 수령 방법 등)는 어떤 외부 API도 제공하지 않음
- 카카오 PK6로 근처 공영·민영 주차장은 조회 가능하나 목표 UI와 다름
- **[미정] 건물 내 주차 정보를 데이터로 제공할 방법 검토 필요** (후보: 공공데이터포털 주차장 정보 API, 카카오 PK6 근처 주차장 + 상세정보 조합)

---

## 요약

| 순서 | API | 핵심 작업 | 외부 API | 상태 |
|------|-----|-----------|----------|------|
| 1 | 지하철 | place_name 파싱, 도보시간 계산, DTO 변경 | 카카오 SW8 | exitNumber 미정 |
| 2 | 버스 | 정류장 + 노선번호 조회 구현 | TAGO or 서울 버스 API | 미구현 |
| 3 | 주차 | 건물 내 주차 정보 데이터 확보 방법 결정 | 미정 | 미정 |
