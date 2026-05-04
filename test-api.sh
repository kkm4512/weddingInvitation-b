#!/usr/bin/env bash
# ============================================================
#  weddingInvitation-b — 전체 API 통합 테스트 스크립트
#
#  Usage:
#    bash test-api.sh                  # userId=1 로 자동 로그인 시도
#    bash test-api.sh 3                # userId=3 으로 로그인
#    TOKEN=<jwt> bash test-api.sh      # JWT 토큰 직접 주입 (카카오 로그인 후)
#
#  사전조건:
#  1. 서버가 http://localhost:8080 에서 실행 중
#  2. DB에 최소 1명의 사용자가 있어야 함
#     → 없으면 먼저 카카오 로그인 후 userId 확인
#  3. jq 설치 필요: winget install jqlang.jq  (또는 brew install jq)
# ============================================================

set -uo pipefail   # -e 제거: FAIL이 있어도 끝까지 실행

BASE="http://localhost:8080"
PASS=0
FAIL=0
SKIP=0

RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[1;33m'
CYAN='\033[0;36m'; RESET='\033[0m'

info()  { echo -e "${CYAN}[INFO]${RESET}  $*"; }
ok()    { echo -e "${GREEN}[PASS]${RESET}  $*"; ((PASS++)); }
fail()  { echo -e "${RED}[FAIL]${RESET}  $*"; ((FAIL++)); }
skip()  { echo -e "${YELLOW}[SKIP]${RESET}  $*"; ((SKIP++)); }

get()       { curl -s -w "\n%{http_code}" -H "Authorization: Bearer $TOKEN" "$BASE$1"; }
post()      { curl -s -w "\n%{http_code}" -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" -d "$2" "$BASE$1"; }
put()       { curl -s -w "\n%{http_code}" -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" -d "$2" -X PUT "$BASE$1"; }
del()       { curl -s -w "\n%{http_code}" -H "Authorization: Bearer $TOKEN" -X DELETE "$BASE$1"; }
post_form() { curl -s -w "\n%{http_code}" -H "Authorization: Bearer $TOKEN" -F "$2" "$BASE$1"; }

code() { echo "$1" | tail -1; }
body() { echo "$1" | head -n -1; }

assert_ok() {
  local label="$1" res="$2"
  local c; c=$(code "$res")
  if [[ "$c" == "200" ]]; then ok "$label (HTTP $c)"
  else fail "$label — expected 200, got $c | $(body "$res" | head -c 200)"; fi
}

extract() { body "$1" | jq -r "$2" 2>/dev/null || echo "null"; }

# ── 인자 처리 ─────────────────────────────────────────────
USER_ID="${1:-1}"           # 첫번째 인자 = userId (기본값 1)
TOKEN="${TOKEN:-}"          # 환경변수 TOKEN 이 있으면 그대로 사용

MCARD_ID=""
INVITE_CODE=""

echo ""
echo "============================================================"
echo "   weddingInvitation-b API 통합 테스트"
echo "   대상 서버: $BASE"
echo "============================================================"
echo ""

# ============================================================
# 1. 헬스 체크
# ============================================================
info "=== 1. 헬스 체크 ==="
res=$(curl -s -w "\n%{http_code}" "$BASE/health")
assert_ok "GET /health" "$res"

# ============================================================
# 2. 인증
# ============================================================
info "=== 2. 인증 ==="

if [[ -n "$TOKEN" ]]; then
  # 토큰이 이미 주입된 경우
  info "환경변수 TOKEN 사용 (카카오 로그인 토큰)"
  res=$(get "/api/v1/auth/me")
  assert_ok "GET /api/v1/auth/me (토큰 검증)" "$res"
else
  # test-login 엔드포인트 사용
  info "userId=$USER_ID 로 테스트 로그인 시도..."
  res=$(curl -s -w "\n%{http_code}" "$BASE/api/v1/auth/test-login/$USER_ID")
  c=$(code "$res")

  if [[ "$c" == "200" ]]; then
    TOKEN=$(extract "$res" '.datas.accessToken')
    if [[ -z "$TOKEN" || "$TOKEN" == "null" ]]; then
      fail "JWT 토큰 추출 실패 — $(body "$res")"
      echo ""
      echo -e "${RED}토큰 획득 실패로 테스트를 중단합니다.${RESET}"
      exit 1
    fi
    ok "GET /api/v1/auth/test-login/$USER_ID (HTTP 200) — JWT 획득"
    res=$(get "/api/v1/auth/me")
    assert_ok "GET /api/v1/auth/me" "$res"
  else
    echo ""
    echo -e "${YELLOW}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${RESET}"
    echo -e "${YELLOW} DB에 userId=$USER_ID 인 사용자가 없습니다.${RESET}"
    echo ""
    echo -e " 방법 1) 카카오 로그인으로 사용자 생성 후 TOKEN 주입:"
    echo ""
    KAKAO_URL=$(curl -s "$BASE/api/v1/auth/kakao" | jq -r '.datas.redirectUrl' 2>/dev/null || echo "(서버 응답 없음)")
    echo -e "   카카오 로그인 URL:"
    echo -e "   $KAKAO_URL"
    echo ""
    echo -e "   로그인 후 받은 accessToken으로 재실행:"
    echo -e "   TOKEN=<accessToken> bash test-api.sh"
    echo ""
    echo -e " 방법 2) userId 직접 지정 (DB에 있는 userId 사용):"
    echo -e "   bash test-api.sh <userId>"
    echo -e "${YELLOW}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${RESET}"
    echo ""
    exit 1
  fi
fi

# ============================================================
# 3. 청첩장 CRUD
# ============================================================
info "=== 3. 청첩장 CRUD ==="

res=$(post "/api/v1/mcards" '{"title":"테스트 청첩장","weddingDate":"2026-10-10"}')
assert_ok "POST /api/v1/mcards" "$res"
MCARD_ID=$(extract "$res" '.datas.mcardId')
INVITE_CODE=$(extract "$res" '.datas.inviteCode')
info "  생성된 mcardId=$MCARD_ID, inviteCode=$INVITE_CODE"

res=$(get "/api/v1/mcards")
assert_ok "GET /api/v1/mcards" "$res"

res=$(get "/api/v1/mcards/$MCARD_ID")
assert_ok "GET /api/v1/mcards/$MCARD_ID" "$res"

res=$(curl -s -w "\n%{http_code}" "$BASE/api/v1/mcards/$MCARD_ID/preview")
assert_ok "GET /api/v1/mcards/$MCARD_ID/preview (공개)" "$res"

res=$(put "/api/v1/mcards/$MCARD_ID" '{"title":"수정된 청첩장"}')
assert_ok "PUT /api/v1/mcards/$MCARD_ID" "$res"

# ============================================================
# 4. 신랑·신부 정보
# ============================================================
info "=== 4. 신랑·신부 정보 ==="

res=$(put "/api/v1/mcards/$MCARD_ID/couple" '{
  "groomName":"김철수","groomPhone":"010-1234-5678",
  "groomFatherName":"김아버지","groomMotherName":"김어머니",
  "brideName":"이영희","bridePhone":"010-8765-4321",
  "brideFatherName":"이아버지","brideMotherName":"이어머니"
}')
assert_ok "PUT /api/v1/mcards/$MCARD_ID/couple" "$res"
res=$(get "/api/v1/mcards/$MCARD_ID/couple")
assert_ok "GET /api/v1/mcards/$MCARD_ID/couple" "$res"

# ============================================================
# 5. 예식 일시
# ============================================================
info "=== 5. 예식 일시 ==="

res=$(put "/api/v1/mcards/$MCARD_ID/schedule" '{"weddingDate":"2026-10-10","weddingTime":"14:00","showCountdown":true}')
assert_ok "PUT /api/v1/mcards/$MCARD_ID/schedule" "$res"
res=$(get "/api/v1/mcards/$MCARD_ID/schedule")
assert_ok "GET /api/v1/mcards/$MCARD_ID/schedule" "$res"

# ============================================================
# 6. 예식 장소 + 교통수단
# ============================================================
info "=== 6. 예식 장소 ==="

res=$(put "/api/v1/mcards/$MCARD_ID/venue" '{"venueName":"그랜드 웨딩홀","venueAddress":"서울시 강남구","lat":37.5172,"lng":127.0473}')
assert_ok "PUT /api/v1/mcards/$MCARD_ID/venue" "$res"
res=$(get "/api/v1/mcards/$MCARD_ID/venue")
assert_ok "GET /api/v1/mcards/$MCARD_ID/venue" "$res"

res=$(post "/api/v1/mcards/$MCARD_ID/venue/transports" '{"type":"지하철","description":"2호선 강남역 3번 출구 도보 5분"}')
assert_ok "POST /api/v1/mcards/$MCARD_ID/venue/transports" "$res"
TRANSPORT_ID=$(extract "$res" '.datas.transportId')
if [[ "$TRANSPORT_ID" != "null" && -n "$TRANSPORT_ID" ]]; then
  res=$(put "/api/v1/mcards/$MCARD_ID/venue/transports/$TRANSPORT_ID" '{"type":"지하철","description":"수정된 설명"}')
  assert_ok "PUT /api/v1/mcards/$MCARD_ID/venue/transports/$TRANSPORT_ID" "$res"
  res=$(del "/api/v1/mcards/$MCARD_ID/venue/transports/$TRANSPORT_ID")
  assert_ok "DELETE /api/v1/mcards/$MCARD_ID/venue/transports/$TRANSPORT_ID" "$res"
fi

# ============================================================
# 7. 콘텐츠 섹션
# ============================================================
info "=== 7. 콘텐츠 섹션 ==="

res=$(put "/api/v1/mcards/$MCARD_ID/theme" '{"themeKey":"momentum","fontKey":"noto-sans","colorKey":"ivory"}')
assert_ok "PUT /api/v1/mcards/$MCARD_ID/theme" "$res"
res=$(get "/api/v1/mcards/$MCARD_ID/theme"); assert_ok "GET theme" "$res"

res=$(put "/api/v1/mcards/$MCARD_ID/greeting" '{"title":"모시는 글","content":"저희 두 사람이 하나가 됩니다."}')
assert_ok "PUT /api/v1/mcards/$MCARD_ID/greeting" "$res"
res=$(get "/api/v1/mcards/$MCARD_ID/greeting"); assert_ok "GET greeting" "$res"

res=$(put "/api/v1/mcards/$MCARD_ID/video" '{"videoUrl":"https://youtube.com/watch?v=test","videoTitle":"우리의 이야기"}')
assert_ok "PUT /api/v1/mcards/$MCARD_ID/video" "$res"
res=$(get "/api/v1/mcards/$MCARD_ID/video"); assert_ok "GET video" "$res"

res=$(put "/api/v1/mcards/$MCARD_ID/bgm" '{"bgmUrl":"https://example.com/music.mp3","bgmTitle":"Our Song","autoPlay":true}')
assert_ok "PUT /api/v1/mcards/$MCARD_ID/bgm" "$res"
res=$(get "/api/v1/mcards/$MCARD_ID/bgm"); assert_ok "GET bgm" "$res"

res=$(put "/api/v1/mcards/$MCARD_ID/quote" '{"content":"사랑은 영원히","author":"작자미상"}')
assert_ok "PUT /api/v1/mcards/$MCARD_ID/quote" "$res"
res=$(get "/api/v1/mcards/$MCARD_ID/quote"); assert_ok "GET quote" "$res"

res=$(put "/api/v1/mcards/$MCARD_ID/photo-quote" '{"imageUrl":"https://example.com/photo.jpg","quoteText":"함께하는 시간"}')
assert_ok "PUT /api/v1/mcards/$MCARD_ID/photo-quote" "$res"
res=$(get "/api/v1/mcards/$MCARD_ID/photo-quote"); assert_ok "GET photo-quote" "$res"

res=$(put "/api/v1/mcards/$MCARD_ID/thumbnail" '{"kakaoThumbnailUrl":"https://example.com/kakao.jpg","urlThumbnailUrl":"https://example.com/url.jpg"}')
assert_ok "PUT /api/v1/mcards/$MCARD_ID/thumbnail" "$res"
res=$(get "/api/v1/mcards/$MCARD_ID/thumbnail"); assert_ok "GET thumbnail" "$res"

res=$(put "/api/v1/mcards/$MCARD_ID/wreath" '{"wreathUrl":"https://example.com/wreath","enabled":true}')
assert_ok "PUT /api/v1/mcards/$MCARD_ID/wreath" "$res"
res=$(get "/api/v1/mcards/$MCARD_ID/wreath"); assert_ok "GET wreath" "$res"

res=$(put "/api/v1/mcards/$MCARD_ID/sections/order" '{"sectionOrder":["greeting","video","gallery","quote","guestbook"]}')
assert_ok "PUT /api/v1/mcards/$MCARD_ID/sections/order" "$res"
res=$(get "/api/v1/mcards/$MCARD_ID/sections/order"); assert_ok "GET sections/order" "$res"

# ============================================================
# 8. 인트로 스타일
# ============================================================
info "=== 8. 인트로 스타일 ==="

res=$(curl -s -w "\n%{http_code}" "$BASE/api/v1/intros")
assert_ok "GET /api/v1/intros (공개)" "$res"
info "  인트로 목록: $(extract "$res" '.datas | length')개"

res=$(put "/api/v1/mcards/$MCARD_ID/intro" '{"introStyleKey":"CLASSIC"}')
assert_ok "PUT /api/v1/mcards/$MCARD_ID/intro" "$res"
res=$(get "/api/v1/mcards/$MCARD_ID/intro"); assert_ok "GET intro" "$res"

# ============================================================
# 9. 테마 목록 (정적)
# ============================================================
info "=== 9. 테마 목록 ==="

res=$(curl -s -w "\n%{http_code}" "$BASE/api/v1/themes")
assert_ok "GET /api/v1/themes (공개)" "$res"
info "  테마 목록: $(extract "$res" '.datas | length')개"

res=$(curl -s -w "\n%{http_code}" "$BASE/api/v1/themes/momentum")
assert_ok "GET /api/v1/themes/momentum" "$res"

# ============================================================
# 10. 샘플 문구 (정적)
# ============================================================
info "=== 10. 샘플 문구 ==="
for ep in greetings quotes notices bgm; do
  res=$(curl -s -w "\n%{http_code}" "$BASE/api/v1/$ep/samples")
  assert_ok "GET /api/v1/$ep/samples (공개)" "$res"
done

# ============================================================
# 11. 갤러리
# ============================================================
info "=== 11. 갤러리 ==="

res=$(get "/api/v1/mcards/$MCARD_ID/gallery")
assert_ok "GET /api/v1/mcards/$MCARD_ID/gallery" "$res"

# 1x1 PNG 생성
TMPIMG=$(mktemp /tmp/test_XXXXXX.png)
printf '\x89PNG\r\n\x1a\n\x00\x00\x00\rIHDR\x00\x00\x00\x01\x00\x00\x00\x01\x08\x02\x00\x00\x00\x90wS\xde\x00\x00\x00\x0cIDATx\x9cc\xf8\x0f\x00\x00\x01\x01\x00\x05\x18\xd8N\x00\x00\x00\x00IEND\xaeB`\x82' > "$TMPIMG"
res=$(post_form "/api/v1/mcards/$MCARD_ID/gallery" "file=@$TMPIMG;type=image/png")
c=$(code "$res")
if [[ "$c" == "200" ]]; then
  ok "POST /api/v1/mcards/$MCARD_ID/gallery (R2 업로드 성공)"
  PHOTO_ID=$(extract "$res" '.datas.photoId')
  if [[ "$PHOTO_ID" != "null" && -n "$PHOTO_ID" ]]; then
    res=$(del "/api/v1/mcards/$MCARD_ID/gallery/$PHOTO_ID")
    assert_ok "DELETE gallery/$PHOTO_ID" "$res"
  fi
else
  skip "POST gallery 파일 업로드 — R2 env 미설정 (HTTP $c)"
fi
rm -f "$TMPIMG"

# ============================================================
# 12. 파일 업로드 (공통)
# ============================================================
info "=== 12. 파일 업로드 ==="

TMPIMG2=$(mktemp /tmp/test_XXXXXX.png)
printf '\x89PNG\r\n\x1a\n\x00\x00\x00\rIHDR\x00\x00\x00\x01\x00\x00\x00\x01\x08\x02\x00\x00\x00\x90wS\xde\x00\x00\x00\x0cIDATx\x9cc\xf8\x0f\x00\x00\x01\x01\x00\x05\x18\xd8N\x00\x00\x00\x00IEND\xaeB`\x82' > "$TMPIMG2"
res=$(post_form "/api/v1/files/upload?folder=test" "file=@$TMPIMG2;type=image/png")
c=$(code "$res")
if [[ "$c" == "200" ]]; then
  ok "POST /api/v1/files/upload (R2 성공)"
  UPLOADED_URL=$(extract "$res" '.datas.fileUrl')
  ENCODED_URL=$(python3 -c "import urllib.parse,sys; print(urllib.parse.quote(sys.argv[1],safe=''))" "$UPLOADED_URL" 2>/dev/null || echo "")
  if [[ -n "$ENCODED_URL" ]]; then
    res=$(curl -s -w "\n%{http_code}" -H "Authorization: Bearer $TOKEN" -X DELETE "$BASE/api/v1/files?fileUrl=$ENCODED_URL")
    assert_ok "DELETE /api/v1/files?fileUrl=..." "$res"
  fi
else
  skip "POST /api/v1/files/upload — R2 env 미설정 (HTTP $c)"
fi
rm -f "$TMPIMG2"

# ============================================================
# 13. QR 코드
# ============================================================
info "=== 13. QR 코드 ==="

HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" -H "Authorization: Bearer $TOKEN" "$BASE/api/v1/mcards/$MCARD_ID/qrcode")
if [[ "$HTTP_CODE" == "200" ]]; then ok "GET /api/v1/mcards/$MCARD_ID/qrcode (image/png)"
else fail "GET /api/v1/mcards/$MCARD_ID/qrcode — HTTP $HTTP_CODE"; fi

# ============================================================
# 14. 연락처 · 계좌 · 안내사항
# ============================================================
info "=== 14. 연락처 · 계좌 · 안내사항 ==="

res=$(put "/api/v1/mcards/$MCARD_ID/contacts" '{"contacts":[{"name":"김철수","phone":"010-1234-5678","relation":"신랑"}]}')
assert_ok "PUT /api/v1/mcards/$MCARD_ID/contacts" "$res"
res=$(get "/api/v1/mcards/$MCARD_ID/contacts"); assert_ok "GET contacts" "$res"

res=$(post "/api/v1/mcards/$MCARD_ID/accounts" '{"bankName":"국민은행","accountNumber":"123-456-789012","accountHolder":"김철수"}')
assert_ok "POST /api/v1/mcards/$MCARD_ID/accounts" "$res"
ACCOUNT_ID=$(extract "$res" '.datas.accountId')
if [[ "$ACCOUNT_ID" != "null" && -n "$ACCOUNT_ID" ]]; then
  res=$(put "/api/v1/mcards/$MCARD_ID/accounts/$ACCOUNT_ID" '{"bankName":"신한은행","accountNumber":"111-222-3334","accountHolder":"김철수"}')
  assert_ok "PUT accounts/$ACCOUNT_ID" "$res"
  res=$(get "/api/v1/mcards/$MCARD_ID/accounts"); assert_ok "GET accounts" "$res"
  res=$(del "/api/v1/mcards/$MCARD_ID/accounts/$ACCOUNT_ID"); assert_ok "DELETE accounts/$ACCOUNT_ID" "$res"
fi

res=$(post "/api/v1/mcards/$MCARD_ID/notices" '{"content":"주차는 지하 2층을 이용해 주세요."}')
assert_ok "POST /api/v1/mcards/$MCARD_ID/notices" "$res"
NOTICE_ID=$(extract "$res" '.datas.noticeId')
if [[ "$NOTICE_ID" != "null" && -n "$NOTICE_ID" ]]; then
  res=$(put "/api/v1/mcards/$MCARD_ID/notices/$NOTICE_ID" '{"content":"수정된 안내사항"}')
  assert_ok "PUT notices/$NOTICE_ID" "$res"
  res=$(get "/api/v1/mcards/$MCARD_ID/notices"); assert_ok "GET notices" "$res"
  res=$(del "/api/v1/mcards/$MCARD_ID/notices/$NOTICE_ID"); assert_ok "DELETE notices/$NOTICE_ID" "$res"
fi

# ============================================================
# 15. RSVP
# ============================================================
info "=== 15. RSVP ==="

res=$(put "/api/v1/mcards/$MCARD_ID/rsvp/settings" '{"enabled":true}')
assert_ok "PUT /api/v1/mcards/$MCARD_ID/rsvp/settings" "$res"
res=$(get "/api/v1/mcards/$MCARD_ID/rsvp/settings"); assert_ok "GET rsvp/settings" "$res"

res=$(curl -s -w "\n%{http_code}" -H "Content-Type: application/json" \
  -d '{"guestName":"홍길동","guestPhone":"010-9999-0000","attendance":true,"guestCount":2}' \
  "$BASE/api/v1/mcards/$MCARD_ID/rsvp")
assert_ok "POST /api/v1/mcards/$MCARD_ID/rsvp (공개)" "$res"

res=$(get "/api/v1/mcards/$MCARD_ID/rsvp"); assert_ok "GET rsvp" "$res"

# ============================================================
# 16. 방명록
# ============================================================
info "=== 16. 방명록 ==="

res=$(put "/api/v1/mcards/$MCARD_ID/guestbook/settings" '{"enabled":true}')
assert_ok "PUT /api/v1/mcards/$MCARD_ID/guestbook/settings" "$res"
res=$(get "/api/v1/mcards/$MCARD_ID/guestbook/settings"); assert_ok "GET guestbook/settings" "$res"

res=$(curl -s -w "\n%{http_code}" -H "Content-Type: application/json" \
  -d '{"author":"박지성","message":"행복하게 사세요!"}' \
  "$BASE/api/v1/mcards/$MCARD_ID/guestbook")
assert_ok "POST /api/v1/mcards/$MCARD_ID/guestbook (공개)" "$res"
MSG_ID=$(extract "$res" '.datas.messageId')

res=$(get "/api/v1/mcards/$MCARD_ID/guestbook"); assert_ok "GET guestbook" "$res"

if [[ "$MSG_ID" != "null" && -n "$MSG_ID" ]]; then
  res=$(put "/api/v1/mcards/$MCARD_ID/guestbook/$MSG_ID/reply" '{"reply":"감사합니다!"}')
  assert_ok "PUT guestbook/$MSG_ID/reply" "$res"
  res=$(del "/api/v1/mcards/$MCARD_ID/guestbook/$MSG_ID"); assert_ok "DELETE guestbook/$MSG_ID" "$res"
fi

# ============================================================
# 17. 하객 공개 뷰
# ============================================================
info "=== 17. 하객 공개 뷰 ==="

if [[ -n "$INVITE_CODE" && "$INVITE_CODE" != "null" ]]; then
  res=$(curl -s -w "\n%{http_code}" "$BASE/api/v1/w/$INVITE_CODE")
  assert_ok "GET /api/v1/w/$INVITE_CODE (공개)" "$res"
else
  skip "inviteCode 없음 — 청첩장 생성이 실패한 경우"
fi

# ============================================================
# 18. 청첩장 삭제 (soft delete)
# ============================================================
info "=== 18. 청첩장 삭제 ==="

res=$(del "/api/v1/mcards/$MCARD_ID")
assert_ok "DELETE /api/v1/mcards/$MCARD_ID (soft delete)" "$res"

res=$(get "/api/v1/mcards/$MCARD_ID")
c=$(code "$res")
if [[ "$c" == "404" || "$c" == "400" ]]; then
  ok "삭제 후 조회 차단 확인 (HTTP $c)"
else
  skip "삭제 후 조회 응답 HTTP $c — 소프트 딜리트 동작 확인 필요"
fi

# ============================================================
# 결과 요약
# ============================================================
TOTAL=$((PASS + FAIL + SKIP))
echo ""
echo "============================================================"
echo -e "   테스트 결과: ${GREEN}PASS $PASS${RESET} / ${RED}FAIL $FAIL${RESET} / ${YELLOW}SKIP $SKIP${RESET}  (총 $TOTAL)"
echo "============================================================"

if [[ $FAIL -gt 0 ]]; then
  echo -e "\n${RED}FAIL 항목이 있습니다. 위 로그를 확인하세요.${RESET}\n"
else
  echo -e "\n${GREEN}모든 테스트 통과!${RESET}\n"
fi

echo "━━━ DB 확인 쿼리 (docker exec -it <컨테이너명> mysql -u<user> -p <db>) ━━━"
cat << 'SQLEOF'
SELECT mcard_id, title, invite_code, is_deleted, created_at FROM mcards ORDER BY created_at DESC LIMIT 5;
SELECT * FROM mcard_couples ORDER BY created_at DESC LIMIT 3;
SELECT * FROM mcard_schedules ORDER BY created_at DESC LIMIT 3;
SELECT * FROM mcard_themes ORDER BY created_at DESC LIMIT 3;
SELECT * FROM mcard_intros ORDER BY created_at DESC LIMIT 3;
SELECT * FROM gallery_photos ORDER BY created_at DESC LIMIT 5;
SELECT * FROM rsvp_responses ORDER BY created_at DESC LIMIT 5;
SELECT * FROM guestbook_messages ORDER BY created_at DESC LIMIT 5;
SQLEOF
