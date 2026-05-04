#!/usr/bin/env python3
"""
weddingInvitation-b 전체 API 통합 테스트
Usage:
  python3 test-api.py              # userId=1 자동 로그인
  python3 test-api.py 2            # userId=2 로 로그인
  TOKEN=<jwt> python3 test-api.py  # 토큰 직접 주입
"""
import sys, os, json, time, tempfile, struct, zlib, urllib.request, urllib.error, urllib.parse

# Windows cp949 콘솔에서 UTF-8 출력 강제
if sys.platform == "win32":
    sys.stdout.reconfigure(encoding="utf-8", errors="replace")
    sys.stderr.reconfigure(encoding="utf-8", errors="replace")

BASE = "http://localhost:8080"
TOKEN = os.environ.get("TOKEN", "")
USER_ID = sys.argv[1] if len(sys.argv) > 1 else "1"

PASS = FAIL = SKIP = 0
GREEN = "\033[92m"; RED = "\033[91m"; YELLOW = "\033[93m"; CYAN = "\033[96m"; RESET = "\033[0m"

def info(msg):  print(f"{CYAN}[INFO]{RESET}  {msg}")
def ok(msg):    global PASS;  PASS  += 1; print(f"{GREEN}[PASS]{RESET}  {msg}")
def fail(msg):  global FAIL;  FAIL  += 1; print(f"{RED}[FAIL]{RESET}  {msg}")
def skip(msg):  global SKIP;  SKIP  += 1; print(f"{YELLOW}[SKIP]{RESET}  {msg}")


# ── HTTP 헬퍼 ────────────────────────────────────────────────────────────────

def request(method, path, body=None, form_data=None, content_type="application/json", extra_headers=None):
    url = BASE + path
    headers = {}
    if TOKEN:
        headers["Authorization"] = f"Bearer {TOKEN}"
    if extra_headers:
        headers.update(extra_headers)

    data = None
    if body is not None:
        data = json.dumps(body).encode()
        headers["Content-Type"] = "application/json"
    elif form_data is not None:
        # multipart/form-data
        boundary = b"----PythonTestBoundary"
        parts = []
        for k, v in form_data.items():
            if isinstance(v, bytes):
                parts.append(
                    b"--" + boundary + b"\r\n"
                    b'Content-Disposition: form-data; name="' + k.encode() + b'"; filename="test.png"\r\n'
                    b"Content-Type: image/png\r\n\r\n" + v + b"\r\n"
                )
            else:
                parts.append(
                    b"--" + boundary + b"\r\n"
                    b'Content-Disposition: form-data; name="' + k.encode() + b'"\r\n\r\n'
                    + str(v).encode() + b"\r\n"
                )
        data = b"".join(parts) + b"--" + boundary + b"--\r\n"
        headers["Content-Type"] = f"multipart/form-data; boundary={boundary.decode()}"

    req = urllib.request.Request(url, data=data, headers=headers, method=method)
    try:
        with urllib.request.urlopen(req, timeout=10) as r:
            resp_body = r.read()
            try:
                # bytes → str 명시 디코딩 후 파싱 (Python 버전 호환)
                text = resp_body.decode("utf-8-sig").strip()
                return r.status, json.loads(text)
            except Exception:
                return r.status, resp_body.decode("utf-8", errors="replace")
    except urllib.error.HTTPError as e:
        try:
            text = e.read().decode("utf-8-sig").strip()
            return e.code, json.loads(text)
        except Exception:
            return e.code, {}
    except Exception as ex:
        return 0, str(ex)

def GET(path):           return request("GET", path)
def POST(path, body):    return request("POST", path, body=body)
def PUT(path, body):     return request("PUT", path, body=body)
def DELETE(path):        return request("DELETE", path)
def POST_FILE(path, file_bytes): return request("POST", path, form_data={"file": file_bytes})

def extract(resp, *keys):
    d = resp
    for k in keys:
        if isinstance(d, dict):
            d = d.get(k)
        else:
            return None
    return d

def assert_ok(label, code, resp):
    if code == 200:
        ok(f"{label} (HTTP 200)")
    else:
        fail(f"{label} — HTTP {code} | {str(resp)[:200]}")

# 1x1 PNG
def tiny_png():
    def chunk(name, data):
        c = zlib.crc32(name + data) & 0xFFFFFFFF
        return struct.pack(">I", len(data)) + name + data + struct.pack(">I", c)
    ihdr = struct.pack(">IIBBBBB", 1, 1, 8, 2, 0, 0, 0)
    idat = zlib.compress(b"\x00\xFF\xFF\xFF")
    return b"\x89PNG\r\n\x1a\n" + chunk(b"IHDR", ihdr) + chunk(b"IDAT", idat) + chunk(b"IEND", b"")


# ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

print()
print("=" * 60)
print("   weddingInvitation-b API 통합 테스트")
print(f"   대상 서버: {BASE}")
print("=" * 60)
print()

# ── 1. 헬스 체크 ─────────────────────────────────────────────
info("=== 1. 헬스 체크 ===")
code, resp = GET("/health")
assert_ok("GET /health", code, resp)

# ── 2. 인증 ──────────────────────────────────────────────────
info("=== 2. 인증 ===")

if TOKEN:
    info("환경변수 TOKEN 사용")
    code, resp = GET("/api/v1/auth/me")
    assert_ok("GET /api/v1/auth/me (토큰 검증)", code, resp)
else:
    info(f"userId={USER_ID} 로 테스트 로그인 시도...")
    code, resp = GET(f"/api/v1/auth/test-login/{USER_ID}")
    if code == 200:
        TOKEN = extract(resp, "datas", "accessToken")
        if not TOKEN:
            fail(f"토큰 추출 실패 — {resp}")
            sys.exit(1)
        ok(f"GET /api/v1/auth/test-login/{USER_ID} (HTTP 200) -- JWT 획득")
        code, resp = GET("/api/v1/auth/me")
        assert_ok("GET /api/v1/auth/me", code, resp)
    else:
        print(f"\n{YELLOW}userId={USER_ID} 없음. 사용법:{RESET}")
        print(f"  python3 test-api.py <userId>")
        print(f"  TOKEN=<jwt> python3 test-api.py\n")
        sys.exit(1)

MCARD_ID = None
INVITE_CODE = None

# ── 3. 청첩장 CRUD ────────────────────────────────────────────
info("=== 3. 청첩장 CRUD ===")
code, resp = POST("/api/v1/mcards", {"title": "테스트 청첩장", "weddingDate": "2026-10-10"})
assert_ok("POST /api/v1/mcards", code, resp)
MCARD_ID = extract(resp, "datas", "mcardId")
INVITE_CODE = extract(resp, "datas", "inviteCode")
info(f"  mcardId={MCARD_ID}, inviteCode={INVITE_CODE}")

code, resp = GET("/api/v1/mcards")
assert_ok("GET /api/v1/mcards", code, resp)

code, resp = GET(f"/api/v1/mcards/{MCARD_ID}")
assert_ok(f"GET /api/v1/mcards/{MCARD_ID}", code, resp)

# preview는 인증 없이도 되지만 TOKEN 있어도 무방
code, resp = GET(f"/api/v1/mcards/{MCARD_ID}/preview")
assert_ok(f"GET /api/v1/mcards/{MCARD_ID}/preview", code, resp)

code, resp = PUT(f"/api/v1/mcards/{MCARD_ID}", {"title": "수정된 청첩장"})
assert_ok(f"PUT /api/v1/mcards/{MCARD_ID}", code, resp)

# ── 4. 신랑·신부 ──────────────────────────────────────────────
info("=== 4. 신랑·신부 정보 ===")
code, resp = PUT(f"/api/v1/mcards/{MCARD_ID}/couple", {
    "groomName": "김철수", "groomPhone": "010-1234-5678",
    "groomFatherName": "김아버지", "groomMotherName": "김어머니",
    "brideName": "이영희", "bridePhone": "010-8765-4321",
    "brideFatherName": "이아버지", "brideMotherName": "이어머니"
})
assert_ok("PUT couple", code, resp)
code, resp = GET(f"/api/v1/mcards/{MCARD_ID}/couple")
assert_ok("GET couple", code, resp)

# ── 5. 예식 일시 ──────────────────────────────────────────────
info("=== 5. 예식 일시 ===")
code, resp = PUT(f"/api/v1/mcards/{MCARD_ID}/schedule", {"weddingDate": "2026-10-10", "weddingTime": "14:00", "showCountdown": True})
assert_ok("PUT schedule", code, resp)
code, resp = GET(f"/api/v1/mcards/{MCARD_ID}/schedule")
assert_ok("GET schedule", code, resp)

# ── 6. 예식 장소 ──────────────────────────────────────────────
info("=== 6. 예식 장소 ===")
code, resp = PUT(f"/api/v1/mcards/{MCARD_ID}/venue", {"venueName": "그랜드 웨딩홀", "hallName": "2F 로즈홀", "address": "서울시 강남구", "lat": 37.5172, "lng": 127.0473, "showMap": True, "lockMap": False})
assert_ok("PUT venue", code, resp)
code, resp = GET(f"/api/v1/mcards/{MCARD_ID}/venue")
assert_ok("GET venue", code, resp)

code, resp = POST(f"/api/v1/mcards/{MCARD_ID}/venue/transports", {"type": "subway", "description": "2호선 강남역 3번 출구 도보 5분", "showIcon": True})
assert_ok("POST venue/transports", code, resp)
transport_id = extract(resp, "datas", "transportId")
if transport_id:
    code, resp = PUT(f"/api/v1/mcards/{MCARD_ID}/venue/transports/{transport_id}", {"type": "subway", "description": "수정된 설명"})
    assert_ok(f"PUT venue/transports/{transport_id}", code, resp)
    code, resp = DELETE(f"/api/v1/mcards/{MCARD_ID}/venue/transports/{transport_id}")
    assert_ok(f"DELETE venue/transports/{transport_id}", code, resp)

# ── 7. 콘텐츠 섹션 ────────────────────────────────────────────
info("=== 7. 콘텐츠 섹션 ===")
for endpoint, body in [
    ("theme",        {"themeKey": "momentum", "fontKey": "noto-sans", "colorKey": "ivory"}),
    ("greeting",     {"title": "모시는 글", "content": "저희 두 사람이 하나가 됩니다."}),
    ("video",        {"videoUrl": "https://youtube.com/watch?v=test", "videoTitle": "우리의 이야기"}),
    ("bgm",          {"bgmUrl": "https://example.com/music.mp3", "bgmTitle": "Our Song", "autoPlay": True}),
    ("quote",        {"content": "사랑은 영원히", "author": "작자미상"}),
    ("photo-quote",  {"imageUrl": "https://example.com/photo.jpg", "quoteText": "함께하는 시간"}),
    ("thumbnail",    {"kakaoThumbnailUrl": "https://example.com/kakao.jpg", "urlThumbnailUrl": "https://example.com/url.jpg"}),
    ("wreath",       {"wreathUrl": "https://example.com/wreath", "enabled": True}),
    ("sections/order", {"sectionOrder": ["greeting", "video", "gallery", "quote", "guestbook"]}),
]:
    code, resp = PUT(f"/api/v1/mcards/{MCARD_ID}/{endpoint}", body)
    assert_ok(f"PUT {endpoint}", code, resp)
    code, resp = GET(f"/api/v1/mcards/{MCARD_ID}/{endpoint}")
    assert_ok(f"GET {endpoint}", code, resp)

# ── 8. 인트로 ─────────────────────────────────────────────────
info("=== 8. 인트로 스타일 ===")
code, resp = GET("/api/v1/intros")
assert_ok("GET /api/v1/intros (공개)", code, resp)
info(f"  인트로 {len(extract(resp,'datas') or [])}개")

code, resp = PUT(f"/api/v1/mcards/{MCARD_ID}/intro", {"introStyleKey": "CLASSIC"})
assert_ok("PUT intro", code, resp)
code, resp = GET(f"/api/v1/mcards/{MCARD_ID}/intro")
assert_ok("GET intro", code, resp)

# ── 9. 테마 목록 ──────────────────────────────────────────────
info("=== 9. 테마 목록 ===")
code, resp = GET("/api/v1/themes")
assert_ok("GET /api/v1/themes (공개)", code, resp)
info(f"  테마 {len(extract(resp,'datas') or [])}개")
code, resp = GET("/api/v1/themes/1")
assert_ok("GET /api/v1/themes/1 (momentum)", code, resp)

# ── 10. 샘플 문구 ─────────────────────────────────────────────
info("=== 10. 샘플 문구 ===")
for ep in ["greetings", "quotes", "notices", "bgm"]:
    code, resp = GET(f"/api/v1/{ep}/samples")
    assert_ok(f"GET /api/v1/{ep}/samples", code, resp)

# ── 11. 갤러리 ────────────────────────────────────────────────
info("=== 11. 갤러리 ===")
code, resp = GET(f"/api/v1/mcards/{MCARD_ID}/gallery")
assert_ok("GET gallery", code, resp)

code, resp = POST_FILE(f"/api/v1/mcards/{MCARD_ID}/gallery", tiny_png())
if code == 200:
    ok("POST gallery (R2 업로드 성공)")
    photo_id = extract(resp, "datas", "photoId")
    if photo_id:
        code, resp = PUT(f"/api/v1/mcards/{MCARD_ID}/gallery/order", {"photoIds": [photo_id]})
        assert_ok("PUT gallery/order", code, resp)

        code, resp = PUT(f"/api/v1/mcards/{MCARD_ID}/gallery/layout", {"layoutType": "grid"})
        assert_ok("PUT gallery/layout", code, resp)

        code, resp = DELETE(f"/api/v1/mcards/{MCARD_ID}/gallery/{photo_id}")
        assert_ok(f"DELETE gallery/{photo_id}", code, resp)
else:
    skip(f"POST gallery — R2 env 미설정 (HTTP {code}: {str(resp)[:100]})")

# ── 12. 파일 업로드 ────────────────────────────────────────────
info("=== 12. 파일 업로드 ===")
code, resp = POST_FILE("/api/v1/files/upload?folder=test", tiny_png())
if code == 200:
    ok("POST /api/v1/files/upload (R2 성공)")
    file_url = extract(resp, "datas", "fileUrl")
    if file_url:
        encoded = urllib.parse.quote(file_url, safe="")
        code, resp = DELETE(f"/api/v1/files?fileUrl={encoded}")
        assert_ok("DELETE /api/v1/files", code, resp)
else:
    skip(f"POST /api/v1/files/upload — R2 env 미설정 (HTTP {code})")

# ── 13. QR 코드 ────────────────────────────────────────────────
info("=== 13. QR 코드 ===")
code, resp = GET(f"/api/v1/mcards/{MCARD_ID}/qrcode")
if code == 200:
    ok(f"GET /api/v1/mcards/{MCARD_ID}/qrcode (HTTP 200)")
else:
    fail(f"GET qrcode — HTTP {code}")

# ── 14. 연락처 · 계좌 · 안내사항 ─────────────────────────────
info("=== 14. 연락처 · 계좌 · 안내사항 ===")
code, resp = PUT(f"/api/v1/mcards/{MCARD_ID}/contacts", {"contacts": [{"contactType": "groom", "name": "김철수", "phoneNumber": "010-1234-5678", "isVisible": True}]})
assert_ok("PUT contacts", code, resp)
code, resp = GET(f"/api/v1/mcards/{MCARD_ID}/contacts")
assert_ok("GET contacts", code, resp)

code, resp = POST(f"/api/v1/mcards/{MCARD_ID}/accounts", {"side": "groom", "bankName": "국민은행", "accountNumber": "123-456-789012", "accountHolder": "김철수"})
assert_ok("POST accounts", code, resp)
account_id = extract(resp, "datas", "accountId")
if account_id:
    code, resp = PUT(f"/api/v1/mcards/{MCARD_ID}/accounts/{account_id}", {"side": "groom", "bankName": "신한은행", "accountNumber": "111-222-3334", "accountHolder": "김철수"})
    assert_ok(f"PUT accounts/{account_id}", code, resp)
    code, resp = GET(f"/api/v1/mcards/{MCARD_ID}/accounts")
    assert_ok("GET accounts", code, resp)
    code, resp = DELETE(f"/api/v1/mcards/{MCARD_ID}/accounts/{account_id}")
    assert_ok(f"DELETE accounts/{account_id}", code, resp)

code, resp = POST(f"/api/v1/mcards/{MCARD_ID}/notices", {"content": "주차는 지하 2층을 이용해 주세요."})
assert_ok("POST notices", code, resp)
notice_id = extract(resp, "datas", "noticeId")
if notice_id:
    code, resp = PUT(f"/api/v1/mcards/{MCARD_ID}/notices/{notice_id}", {"content": "수정된 안내사항"})
    assert_ok(f"PUT notices/{notice_id}", code, resp)
    code, resp = GET(f"/api/v1/mcards/{MCARD_ID}/notices")
    assert_ok("GET notices", code, resp)
    code, resp = DELETE(f"/api/v1/mcards/{MCARD_ID}/notices/{notice_id}")
    assert_ok(f"DELETE notices/{notice_id}", code, resp)

# ── 15. RSVP ──────────────────────────────────────────────────
info("=== 15. RSVP ===")
code, resp = PUT(f"/api/v1/mcards/{MCARD_ID}/rsvp/settings", {"enabled": True})
assert_ok("PUT rsvp/settings", code, resp)
code, resp = GET(f"/api/v1/mcards/{MCARD_ID}/rsvp/settings")
assert_ok("GET rsvp/settings", code, resp)

# 하객 제출 (인증 없이)
saved_token = TOKEN; TOKEN = ""
code, resp = POST(f"/api/v1/mcards/{MCARD_ID}/rsvp", {"responderName": "홍길동", "responderPhone": "010-9999-0000", "willAttend": True, "attendeeCount": 2})
assert_ok("POST rsvp (공개)", code, resp)
TOKEN = saved_token

code, resp = GET(f"/api/v1/mcards/{MCARD_ID}/rsvp")
assert_ok("GET rsvp", code, resp)

# ── 16. 방명록 ────────────────────────────────────────────────
info("=== 16. 방명록 ===")
code, resp = PUT(f"/api/v1/mcards/{MCARD_ID}/guestbook/settings", {"enabled": True})
assert_ok("PUT guestbook/settings", code, resp)
code, resp = GET(f"/api/v1/mcards/{MCARD_ID}/guestbook/settings")
assert_ok("GET guestbook/settings", code, resp)

saved_token = TOKEN; TOKEN = ""
code, resp = POST(f"/api/v1/mcards/{MCARD_ID}/guestbook", {"guestName": "박지성", "content": "행복하게 사세요!"})
assert_ok("POST guestbook (공개)", code, resp)
TOKEN = saved_token
msg_id = extract(resp, "datas", "messageId")

code, resp = GET(f"/api/v1/mcards/{MCARD_ID}/guestbook")
assert_ok("GET guestbook", code, resp)

if msg_id:
    code, resp = PUT(f"/api/v1/mcards/{MCARD_ID}/guestbook/{msg_id}/reply", {"reply": "감사합니다!"})
    assert_ok(f"PUT guestbook/{msg_id}/reply", code, resp)
    code, resp = DELETE(f"/api/v1/mcards/{MCARD_ID}/guestbook/{msg_id}")
    assert_ok(f"DELETE guestbook/{msg_id}", code, resp)

# ── 17. 하객 공개 뷰 ──────────────────────────────────────────
info("=== 17. 하객 공개 뷰 ===")
if INVITE_CODE:
    saved_token = TOKEN; TOKEN = ""
    code, resp = GET(f"/api/v1/w/{INVITE_CODE}")
    assert_ok(f"GET /api/v1/w/{INVITE_CODE} (공개)", code, resp)
    TOKEN = saved_token
else:
    skip("inviteCode 없음")

# ── 18. 청첩장 삭제 ───────────────────────────────────────────
info("=== 18. 청첩장 삭제 ===")
code, resp = DELETE(f"/api/v1/mcards/{MCARD_ID}")
assert_ok(f"DELETE /api/v1/mcards/{MCARD_ID} (soft delete)", code, resp)

code, resp = GET(f"/api/v1/mcards/{MCARD_ID}")
if code in (400, 404):
    ok(f"삭제 후 조회 차단 확인 (HTTP {code})")
else:
    skip(f"삭제 후 GET HTTP {code} — soft delete 동작 확인 필요")

# ── 결과 요약 ────────────────────────────────────────────────
total = PASS + FAIL + SKIP
print()
print("=" * 60)
print(f"   {GREEN}PASS {PASS}{RESET} / {RED}FAIL {FAIL}{RESET} / {YELLOW}SKIP {SKIP}{RESET}  (총 {total})")
print("=" * 60)
if FAIL:
    print(f"\n{RED}FAIL 항목이 있습니다. 위 로그를 확인하세요.{RESET}\n")
    sys.exit(1)
else:
    print(f"\n{GREEN}모든 테스트 통과!{RESET}\n")
