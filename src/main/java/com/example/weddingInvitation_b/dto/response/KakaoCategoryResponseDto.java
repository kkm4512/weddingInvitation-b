package com.example.weddingInvitation_b.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 카카오 카테고리 검색 API 원시 응답 DTO
 *
 * <p>https://dapi.kakao.com/v2/local/search/category.json 응답 구조를 그대로 매핑한다.
 * 키워드 검색과 달리 {@code distance} 필드가 추가로 제공된다.
 * 이 DTO는 카카오 API와의 통신 전용이며, 컨트롤러 응답에는 직접 사용하지 않는다.</p>
 *
 * @see TransportSuggestionsResponseDto 프론트엔드 응답용 DTO
 */
@Getter
@NoArgsConstructor
public class KakaoCategoryResponseDto {

    /** 검색 결과 문서 목록 */
    private List<Document> documents;

    /** 검색 결과 메타 정보 */
    private Meta meta;

    /**
     * 카테고리 검색 결과 단건
     */
    @Getter
    @NoArgsConstructor
    public static class Document {

        /** 장소명 (예: 역삼역 3번출구, 강남역.신분당선) */
        @JsonProperty("place_name")
        private String placeName;

        /** 도로명 주소 */
        @JsonProperty("road_address_name")
        private String roadAddressName;

        /** 지번 주소 */
        @JsonProperty("address_name")
        private String addressName;

        /** 경도 (longitude) */
        private String x;

        /** 위도 (latitude) */
        private String y;

        /**
         * 중심 좌표로부터의 직선거리 (m 단위 문자열, 예: "320")
         *
         * <p>카카오 카테고리 검색에서만 제공되는 필드.
         * sort=distance 파라미터와 함께 사용하면 거리 오름차순으로 정렬된다.</p>
         */
        private String distance;

        /** 카테고리 그룹 코드 (예: SW8, BS8) */
        @JsonProperty("category_group_code")
        private String categoryGroupCode;

        /** 카테고리 그룹명 (예: 지하철역, 버스정류장) */
        @JsonProperty("category_group_name")
        private String categoryGroupName;

        /**
         * 카테고리 전체 경로 (예: "지하철 > 수도권 > 7호선")
         *
         * <p>노선명 파싱에 활용한다.</p>
         */
        @JsonProperty("category_name")
        private String categoryName;
    }

    /**
     * 검색 결과 메타 정보
     */
    @Getter
    @NoArgsConstructor
    public static class Meta {

        /** 검색어 매칭 전체 문서 수 */
        @JsonProperty("total_count")
        private Integer totalCount;

        /** 현재 페이지 노출 가능 문서 수 */
        @JsonProperty("pageable_count")
        private Integer pageableCount;

        /** 마지막 페이지 여부 */
        @JsonProperty("is_end")
        private Boolean isEnd;
    }
}
