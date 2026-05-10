package com.example.weddingInvitation_b.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * 지하철역 출구 단건 응답 DTO
 *
 * <p>카카오 키워드 검색 API를 통해 조회한 지하철 출구 정보를 담는다.
 * 출구별 좌표로 도보 거리를 계산하여 가장 가까운 출구 정보를 반환한다.</p>
 *
 * <p>예시 응답:</p>
 * <pre>
 * {
 *   "lineName": "수인분당선",
 *   "stationName": "서울숲역",
 *   "exitNumber": "5번 출구",
 *   "walkingMinutes": 1
 * }
 * </pre>
 *
 * @see com.example.weddingInvitation_b.service.impl.TransportServiceImpl
 */
@Getter
@Builder
public class SubwayItem {

    /**
     * 노선명 (예: "수인분당선", "2호선")
     *
     * <p>카카오 키워드 검색 결과 place_name에서 파싱한다.
     * "서울숲역 수인분당선 5번출구" → "수인분당선"</p>
     */
    private String lineName;

    /**
     * 역명 (예: "서울숲역")
     *
     * <p>카카오 키워드 검색 결과 place_name에서 "역" 이전 문자열을 파싱한다.</p>
     */
    private String stationName;

    /**
     * 출구 번호 (예: "5번 출구")
     *
     * <p>카카오 키워드 검색 결과 place_name에서 파싱한다.
     * "5번출구" → "5번 출구"</p>
     */
    private String exitNumber;

    /**
     * 예식장에서 해당 출구까지 도보 소요 시간 (분, 올림 처리)
     *
     * <p>출구 좌표 기반 직선거리 ÷ 67m/분으로 계산하며, 최솟값은 1분이다.</p>
     */
    private Integer walkingMinutes;
}
