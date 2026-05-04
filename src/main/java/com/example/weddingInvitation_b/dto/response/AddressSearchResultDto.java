package com.example.weddingInvitation_b.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 주소 검색 결과 응답 DTO
 *
 * <p>카카오 로컬 API 원시 응답에서 프론트엔드에 필요한 정보만 추려서 반환한다.
 * 카카오 API 내부 구조는 이 DTO에 노출되지 않는다.</p>
 *
 * @see KakaoAddressResponseDto 카카오 API 원시 응답
 */
@Getter
@Builder
public class AddressSearchResultDto {

    /** 검색된 주소 목록 */
    private List<AddressItem> addresses;

    /**
     * 장소/주소 단건 결과
     */
    @Getter
    @Builder
    public static class AddressItem {

        /** 장소명 (예: 역삼역 2호선). 장소명이 없으면 null */
        private String placeName;

        /** 대표 주소명 (도로명 주소 우선, 없으면 지번 주소) */
        private String addressName;

        /** 도로명 주소 (없을 수 있음) */
        private String roadAddress;

        /** 지번 주소 (없을 수 있음) */
        private String jibunAddress;

        /** 위도 */
        private Double latitude;

        /** 경도 */
        private Double longitude;
    }

    /**
     * 카카오 키워드 검색 응답을 프론트엔드용 DTO로 변환
     *
     * <p>카카오 응답의 x(경도)/y(위도) 문자열을 Double로 변환하고,
     * 장소명·도로명·지번 주소를 각각 분리하여 반환한다.</p>
     *
     * @param kakaoResponse 카카오 키워드 검색 API 원시 응답
     * @return 프론트엔드용 주소 목록 DTO
     */
    public static AddressSearchResultDto from(KakaoAddressResponseDto kakaoResponse) {
        if (kakaoResponse == null || kakaoResponse.getDocuments() == null) {
            return AddressSearchResultDto.builder()
                .addresses(Collections.emptyList())
                .build();
        }

        List<AddressItem> items = kakaoResponse.getDocuments().stream()
            .map(doc -> {
                // 위도(y), 경도(x) 문자열 → Double 변환
                Double lat = parseCoordinate(doc.getY());
                Double lng = parseCoordinate(doc.getX());

                // 대표 주소: 도로명 주소 우선, 없으면 지번 주소
                String representative = (doc.getRoadAddressName() != null && !doc.getRoadAddressName().isBlank())
                    ? doc.getRoadAddressName()
                    : doc.getAddressName();

                return AddressItem.builder()
                    .placeName(doc.getPlaceName())
                    .addressName(representative)
                    .roadAddress(doc.getRoadAddressName())
                    .jibunAddress(doc.getAddressName())
                    .latitude(lat)
                    .longitude(lng)
                    .build();
            })
            .collect(Collectors.toList());

        return AddressSearchResultDto.builder()
            .addresses(items)
            .build();
    }

    /**
     * 좌표 문자열을 Double로 변환
     *
     * @param value 좌표 문자열 (null 또는 빈 문자열 가능)
     * @return Double 좌표값, 변환 불가 시 null
     */
    private static Double parseCoordinate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
