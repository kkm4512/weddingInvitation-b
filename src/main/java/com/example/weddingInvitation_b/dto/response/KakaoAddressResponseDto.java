package com.example.weddingInvitation_b.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 카카오 키워드 검색 API 원시 응답 DTO
 *
 * <p>https://dapi.kakao.com/v2/local/search/keyword.json 응답 구조를 그대로 매핑한다.
 * 주소 검색 API와 달리 장소명(역삼역, 예식홀 등)으로도 검색 가능하다.
 * 이 DTO는 카카오 API와의 통신 전용이며, 컨트롤러 응답에는 직접 사용하지 않는다.</p>
 *
 * @see AddressSearchResultDto 프론트엔드 응답용 DTO
 */
@Getter
@NoArgsConstructor
public class KakaoAddressResponseDto {

    /** 검색 결과 문서 목록 */
    private List<Document> documents;

    /** 검색 결과 메타 정보 */
    private Meta meta;

    /**
     * 키워드 검색 결과 단건
     *
     * <p>카카오 키워드 검색 API는 address_name, road_address_name, x, y를
     * Document 최상위 필드로 바로 제공한다.
     * x, y, radius 파라미터와 함께 요청하면 distance 필드도 함께 제공된다.</p>
     */
    @Getter
    @NoArgsConstructor
    public static class Document {

        /** 장소명 (예: 역삼역 2호선) */
        @JsonProperty("place_name")
        private String placeName;

        /** 지번 주소 (예: 서울 강남구 역삼동 678) */
        @JsonProperty("address_name")
        private String addressName;

        /** 도로명 주소 (예: 서울 강남구 강남대로 396) */
        @JsonProperty("road_address_name")
        private String roadAddressName;

        /** 경도 (longitude) */
        private String x;

        /** 위도 (latitude) */
        private String y;

        /**
         * 카테고리 전체 경로 (예: 교통,수송 > 버스 > 시내버스)
         *
         * <p>버스정류장 필터링 시 이 필드가 "교통,수송"으로 시작하는지 확인한다.
         * 카카오 로컬 API에 버스정류장 category_group_code(BS8)가 없으므로
         * category_name으로 교통 카테고리 여부를 판별한다.</p>
         */
        @JsonProperty("category_name")
        private String categoryName;

        /**
         * 중요 카테고리 그룹 코드 (예: SW8, FD6)
         *
         * <p>카카오 공식 18개 카테고리에 해당하는 경우에만 값이 존재한다.
         * 버스정류장은 공식 카테고리 코드가 없으므로 이 필드는 빈 문자열("")이다.</p>
         */
        @JsonProperty("category_group_code")
        private String categoryGroupCode;

        /**
         * 중요 카테고리 그룹명 (예: 지하철역, 음식점)
         *
         * <p>category_group_code에 대응하는 한글 그룹명.
         * 버스정류장은 공식 카테고리 코드가 없으므로 이 필드는 빈 문자열("")이다.</p>
         */
        @JsonProperty("category_group_name")
        private String categoryGroupName;

        /**
         * 중심 좌표로부터의 직선거리 (m 단위 문자열, 예: "320")
         *
         * <p>x, y, radius 파라미터를 포함한 요청에서만 제공된다.
         * 버스정류장 키워드 검색 시 거리 정렬·표시에 사용한다.</p>
         */
        private String distance;
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
