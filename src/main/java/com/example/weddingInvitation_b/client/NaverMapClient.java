package com.example.weddingInvitation_b.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * 네이버 Static Map API 통신 클라이언트
 *
 * <p>위경도 좌표를 받아 네이버 지도 이미지(PNG)를 반환한다.</p>
 *
 * <ul>
 *   <li>API URL: https://maps.apigw.ntruss.com/map-static/v2/raster</li>
 *   <li>인증 헤더: x-ncp-apigw-api-key-id, x-ncp-apigw-api-key</li>
 *   <li>좌표 순서: center=경도(lng),위도(lat)</li>
 *   <li>URI 빌드: java.net.URI 5-arg 생성자 사용 — Spring UriComponentsBuilder 우회 필수
 *       (String 전달 시 UriComponentsBuilder가 이미 인코딩된 %7C/%20을 디코딩 후 재인코딩하여 변형)</li>
 *   <li>무료 한도: 월 300만 건</li>
 * </ul>
 */
@Slf4j
@Component
public class NaverMapClient {

    private static final String NAVER_STATIC_MAP_HOST = "maps.apigw.ntruss.com";
    private static final String NAVER_STATIC_MAP_PATH = "/map-static/v2/raster";

    @Value("${naver.map.client.id}")
    private String clientId;

    @Value("${naver.map.client.secret}")
    private String clientSecret;

    private final RestClient restClient;

    public NaverMapClient() {
        this.restClient = RestClient.create();
    }

    /**
     * 위경도 좌표 기반 네이버 지도 이미지 조회
     *
     * <p>지정한 좌표를 중심으로 하는 지도 이미지를 PNG 바이트 배열로 반환한다.
     * 마커는 중심 좌표에 자동 표시된다.</p>
     *
     * <p>URI는 {@code java.net.URI(scheme, host, path, query, fragment)} 생성자로 빌드한다.
     * 이 생성자는 raw 쿼리값의 특수문자(|, 공백 등)를 RFC 3986에 따라 자동 인코딩하며,
     * 결과 URI 객체를 RestClient에 전달하면 Spring의 URI 파싱 없이 그대로 전송된다.</p>
     *
     * @param lat    위도 (latitude)
     * @param lng    경도 (longitude)
     * @param width  이미지 너비 (px)
     * @param height 이미지 높이 (px)
     * @return 지도 이미지 바이트 배열 (PNG)
     * @throws RuntimeException 네이버 API 호출 실패 시
     */
    public byte[] getStaticMapImage(double lat, double lng, int width, int height) {
        // raw 쿼리 문자열: | 와 공백을 그대로 사용 — URI 생성자가 인코딩 처리
        // markers 형식: type:d|size:mid|pos:{경도} {위도}  (경도 먼저)
        String rawQuery = "w=" + width
            + "&h=" + height
            + "&center=" + lng + "," + lat
            + "&level=16"
            + "&markers=type:d|size:mid|pos:" + lng + " " + lat;

        URI uri;
        try {
            // 5-arg URI 생성자: 특수문자를 RFC 3986에 따라 자동 인코딩
            // | → %7C, 공백 → %20
            uri = new URI("https", NAVER_STATIC_MAP_HOST, NAVER_STATIC_MAP_PATH, rawQuery, null);
        } catch (URISyntaxException e) {
            throw new RuntimeException("네이버 지도 URI 생성 실패: " + e.getMessage(), e);
        }

        log.info("[NaverMapClient] Static Map 요청 URI: {}", uri);

        try {
            // URI 객체를 직접 전달 → Spring UriComponentsBuilder 파싱 우회
            RestClient.ResponseSpec responseSpec = restClient.get()
                .uri(uri)
                .header("x-ncp-apigw-api-key-id", clientId)
                .header("x-ncp-apigw-api-key", clientSecret)
                .retrieve();

            byte[] imageBytes = responseSpec.body(byte[].class);

            if (imageBytes == null || imageBytes.length == 0) {
                throw new RuntimeException("네이버 지도 이미지를 받지 못했습니다.");
            }

            log.info("[NaverMapClient] Static Map 이미지 수신 완료 ({}bytes)", imageBytes.length);
            return imageBytes;

        } catch (RestClientException e) {
            log.error("[NaverMapClient] Static Map 오류: {}", e.getMessage(), e);
            throw new RuntimeException("네이버 지도 이미지 조회 중 오류가 발생했습니다.", e);
        }
    }
}
