package com.example.weddingInvitation_b.controller;

import com.example.weddingInvitation_b.dto.response.ApiResponse;
import com.example.weddingInvitation_b.dto.response.SampleTextDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 샘플 문구 및 BGM 샘플 API 컨트롤러
 *
 * <p>인사말, 글귀, 안내사항, BGM 샘플 목록을 반환한다.
 * 정적 데이터로 관리되며 인증이 불필요하다.</p>
 */
@RestController
@RequestMapping("/api/v1")
public class SampleController {

    // ── 인사말 샘플 ─────────────────────────────────────────────────

    private static final List<SampleTextDto> GREETING_SAMPLES = List.of(
        SampleTextDto.builder().sampleId("g1").title("전통형")
            .content("저희 두 사람이 사랑을 나누며\n평생 함께하는 반려자가 되려 합니다.\n귀한 걸음 하시어 저희의 앞날을 축복해 주시면 감사하겠습니다.").build(),
        SampleTextDto.builder().sampleId("g2").title("모던형")
            .content("서로 다른 두 사람이 만나\n하나의 새로운 가정을 이루게 되었습니다.\n소중한 분들과 함께 기쁨을 나누고 싶습니다.").build(),
        SampleTextDto.builder().sampleId("g3").title("감성형")
            .content("봄날의 설렘처럼\n우리 둘의 인연이 꽃을 피웠습니다.\n눈부신 그 순간을 함께해 주세요.").build(),
        SampleTextDto.builder().sampleId("g4").title("간결형")
            .content("평생을 함께할 사람을 만났습니다.\n빛나는 날, 함께해 주세요.").build(),
        SampleTextDto.builder().sampleId("g5").title("가족형")
            .content("두 가족이 하나가 되는 날\n행복한 만남을 나누고자 합니다.\n따뜻한 마음으로 함께해 주시면 감사하겠습니다.").build()
    );

    // ── 글귀 샘플 ────────────────────────────────────────────────────

    private static final List<SampleTextDto> QUOTE_SAMPLES = List.of(
        SampleTextDto.builder().sampleId("q1").title(null)
            .content("사랑은 서로 마주 보는 것이 아니라,\n함께 같은 방향을 바라보는 것이다.\n— 앙투안 드 생텍쥐페리").build(),
        SampleTextDto.builder().sampleId("q2").title(null)
            .content("당신을 사랑하는 것은\n나를 가장 나답게 만드는 일입니다.").build(),
        SampleTextDto.builder().sampleId("q3").title(null)
            .content("두 사람이 함께라면\n모든 길이 집으로 향하는 길입니다.").build(),
        SampleTextDto.builder().sampleId("q4").title(null)
            .content("오늘 이 순간부터\n우리는 서로의 가장 아름다운 이야기가 됩니다.").build(),
        SampleTextDto.builder().sampleId("q5").title(null)
            .content("사랑한다는 것은\n같이 늙어가기로 약속하는 것입니다.\n— 폴 뉴먼").build()
    );

    // ── 안내사항 샘플 ────────────────────────────────────────────────

    private static final List<SampleTextDto> NOTICE_SAMPLES = List.of(
        SampleTextDto.builder().sampleId("n1").title("주차 안내")
            .content("예식장 내 지하 주차장을 이용하실 수 있습니다.\n주차권은 식장 입구에서 받으실 수 있습니다.").build(),
        SampleTextDto.builder().sampleId("n2").title("식사 안내")
            .content("예식 후 피로연이 준비되어 있습니다.\n많이 참석하시어 자리를 빛내 주시면 감사하겠습니다.").build(),
        SampleTextDto.builder().sampleId("n3").title("대중교통 안내")
            .content("지하철 이용 시 ○○역 ○번 출구에서 도보 5분 거리입니다.\n버스 이용 시 ○○정류장에서 하차하시면 됩니다.").build(),
        SampleTextDto.builder().sampleId("n4").title("드레스코드")
            .content("편안하게 오세요. 별도의 드레스코드는 없습니다.\n다만 흰색 계열 의상은 자제해 주시면 감사하겠습니다.").build(),
        SampleTextDto.builder().sampleId("n5").title("사진 촬영 안내")
            .content("예식 중 사진 촬영은 공식 포토그래퍼에게만 허용됩니다.\n피로연 중에는 자유롭게 촬영하셔도 됩니다.").build()
    );

    // ── BGM 샘플 ─────────────────────────────────────────────────────

    private static final List<SampleTextDto> BGM_SAMPLES = List.of(
        SampleTextDto.builder().sampleId("b1").title("클래식")
            .content("Pachelbel - Canon in D Major").build(),
        SampleTextDto.builder().sampleId("b2").title("재즈")
            .content("Frank Sinatra - The Way You Look Tonight").build(),
        SampleTextDto.builder().sampleId("b3").title("팝")
            .content("John Legend - All of Me").build(),
        SampleTextDto.builder().sampleId("b4").title("국내 발라드")
            .content("거미 - 기억해줘요 내 모든 날과 그때를").build(),
        SampleTextDto.builder().sampleId("b5").title("피아노")
            .content("Yiruma - River Flows in You").build(),
        SampleTextDto.builder().sampleId("b6").title("국내 팝")
            .content("이소라 - 바람이 분다").build()
    );

    /**
     * 인사말 샘플 문구 목록 조회
     *
     * @return 인사말 샘플 목록
     */
    @GetMapping("/greetings/samples")
    public ApiResponse<List<SampleTextDto>> getGreetingSamples() {
        return ApiResponse.success(GREETING_SAMPLES);
    }

    /**
     * 글귀 샘플 문구 목록 조회
     *
     * @return 글귀 샘플 목록
     */
    @GetMapping("/quotes/samples")
    public ApiResponse<List<SampleTextDto>> getQuoteSamples() {
        return ApiResponse.success(QUOTE_SAMPLES);
    }

    /**
     * 안내사항 샘플 문구 목록 조회
     *
     * @return 안내사항 샘플 목록
     */
    @GetMapping("/notices/samples")
    public ApiResponse<List<SampleTextDto>> getNoticeSamples() {
        return ApiResponse.success(NOTICE_SAMPLES);
    }

    /**
     * 배경음악 샘플 목록 조회
     *
     * @return BGM 샘플 목록
     */
    @GetMapping("/bgm/samples")
    public ApiResponse<List<SampleTextDto>> getBgmSamples() {
        return ApiResponse.success(BGM_SAMPLES);
    }
}
