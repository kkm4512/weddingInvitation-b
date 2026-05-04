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
     * Document 최상위 필드로 바로 제공한다.</p>
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

        /** 카테고리 (예: 교통,수송 > 지하철,전철 > 수도권전철 2호선) */
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
