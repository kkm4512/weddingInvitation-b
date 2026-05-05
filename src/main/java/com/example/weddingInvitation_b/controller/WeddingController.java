package com.example.weddingInvitation_b.controller;

import com.example.weddingInvitation_b.dto.response.*;
import com.example.weddingInvitation_b.service.McardPhotoQuoteService;
import com.example.weddingInvitation_b.service.McardBgmService;
import com.example.weddingInvitation_b.service.McardNoticeService;
import com.example.weddingInvitation_b.service.McardSectionOrderService;
import com.example.weddingInvitation_b.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 청첩장 공개 뷰 컨트롤러 (하객용)
 *
 * <p>초대 코드를 통해 인증 없이 청첩장을 조회하는 공개 API를 제공한다.
 * SecurityConfig에서 이 경로는 permitAll() 처리되어야 한다.</p>
 *
 * @see McardService
 */
@RestController
@RequestMapping("/api/v1/w")
@RequiredArgsConstructor
public class WeddingController {

    private final McardService mcardService;
    private final McardCoupleService mcardCoupleService;
    private final McardScheduleService mcardScheduleService;
    private final McardVenueService mcardVenueService;
    private final McardGreetingService mcardGreetingService;
    private final McardQuoteService mcardQuoteService;
    private final McardVideoService mcardVideoService;
    private final GalleryService galleryService;
    private final BankAccountService bankAccountService;
    private final McardContactService mcardContactService;
    private final RsvpService rsvpService;
    private final GuestbookService guestbookService;
    private final McardWreathService mcardWreathService;
    private final McardPhotoQuoteService mcardPhotoQuoteService;
    private final McardBgmService mcardBgmService;
    private final McardNoticeService mcardNoticeService;
    private final McardSectionOrderService mcardSectionOrderService;

    /**
     * 초대 코드로 공개 청첩장 조회 (하객 뷰)
     *
     * <p>인증 없이 접근 가능하다. inviteCode에 해당하는 청첩장 전체 데이터를 반환한다.</p>
     *
     * @param inviteCode 청첩장 초대 코드 (공개 URL)
     * @return 청첩장 공개 데이터 (섹션 순서 적용)
     */
    @GetMapping("/{inviteCode}")
    public ApiResponse<Map<String, Object>> getPublicMcard(@PathVariable String inviteCode) {
        McardResponseDto mcard = mcardService.getMcardByInviteCode(inviteCode);
        Long mcardId = mcard.getMcardId();

        // 모든 섹션 데이터를 미리 준비
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("couple", safe(() -> mcardCoupleService.getCouple(mcardId)));
        response.put("greeting", safe(() -> mcardGreetingService.getGreeting(mcardId)));
        response.put("schedule", translateSchedule(safe(() -> mcardScheduleService.getSchedule(mcardId))));
        response.put("venue", translateVenue(safe(() -> mcardVenueService.getVenue(mcardId))));
        response.put("quote", safe(() -> mcardQuoteService.getQuote(mcardId)));
        response.put("video", safe(() -> mcardVideoService.getVideo(mcardId)));
        response.put("wreath", safe(() -> mcardWreathService.getWreath(mcardId)));
        response.put("photoQuote", safe(() -> mcardPhotoQuoteService.getPhotoQuote(mcardId)));
        response.put("bgm", safe(() -> mcardBgmService.getBgm(mcardId)));
        response.put("notices", safe(() -> mcardNoticeService.getNotices(mcardId)));
        response.put("gallery", getGalleryImages(mcardId));
        response.put("accounts", safe(() -> bankAccountService.getAccounts(mcardId)));
        response.put("contacts", safe(() -> mcardContactService.getContacts(mcardId)));
        response.put("rsvpSettings", safe(() -> rsvpService.getSetting(mcardId)));
        response.put("guestbookSettings", safe(() -> guestbookService.getSetting(mcardId)));
        response.put("guestbookMessages", safe(() -> guestbookService.getMessages(mcardId)));
        response.put("mcardId", mcardId);
        response.put("title", mcard.getTitle());
        response.put("inviteCode", mcard.getInviteCode());
        response.put("hasWatermark", mcard.getHasWatermark());
        response.put("createdAt", mcard.getCreatedAt());
        response.put("updatedAt", mcard.getUpdatedAt());

        // 섹션 순서 가져오기 (저장된 순서가 없으면 기본값 사용)
        McardSectionOrderResponseDto sectionOrderDto = mcardSectionOrderService.getSectionOrder(mcardId);
        List<String> sectionOrder = sectionOrderDto.getSectionOrder();

        // 섹션 순서를 응답에 포함 (프론트에서 렌더링 순서로 사용)
        response.put("sectionOrder", sectionOrder);
        return ApiResponse.success(response);
    }

    private <T> T safe(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception ex) {
            return null;
        }
    }

    private Map<String, Object> translateSchedule(McardScheduleResponseDto schedule) {
        if (schedule == null) {
            return null;
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("scheduleId", schedule.getScheduleId());
        result.put("mcardId", schedule.getMcardId());
        result.put("weddingDateTime", schedule.getWeddingDateTime());
        if (schedule.getWeddingDateTime() != null) {
            result.put("weddingDate", schedule.getWeddingDateTime().toLocalDate().toString());
            result.put("weddingTime", schedule.getWeddingDateTime().toLocalTime().toString());
        }
        result.put("prepTimeMinutes", schedule.getPrepTimeMinutes());
        result.put("showCalendar", Boolean.TRUE);
        return result;
    }

    private Map<String, Object> translateVenue(McardVenueResponseDto venue) {
        if (venue == null) {
            return null;
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("venueId", venue.getVenueId());
        result.put("mcardId", venue.getMcardId());
        result.put("venueName", venue.getVenueName());
        result.put("hallName", venue.getFloorInfo());
        result.put("address", venue.getAddress());
        result.put("lat", venue.getLatitude());
        result.put("lng", venue.getLongitude());
        // PUT 저장 시 서버가 네이버 Static Map 이미지를 자동 생성하여 R2에 업로드한 URL
        // 프론트: <img src="mapImageUrl"> 로 바로 사용
        result.put("mapImageUrl", venue.getMapImageUrl());
        result.put("showMap", venue.getShowMap());
        result.put("mapLocked", venue.getMapLocked());
        result.put("showTransportIcons", venue.getShowTransportIcons());
        result.put("transports", translateTransports(venue.getTransports()));
        return result;
    }

    private List<Map<String, Object>> translateTransports(List<VenueTransportResponseDto> transports) {
        if (transports == null) {
            return List.of();
        }

        return transports.stream()
            .map(transport -> Map.<String, Object>of(
                "transportId", transport.getTransportId(),
                "type", transport.getTransportType(),
                "description", transport.getDescription(),
                "displayOrder", transport.getDisplayOrder()))
            .collect(Collectors.toList());
    }

    private List<String> getGalleryImages(Long mcardId) {
        List<GalleryPhotoResponseDto> gallery = safe(() -> galleryService.getGallery(mcardId));
        if (gallery == null) {
            return List.of();
        }

        return gallery.stream()
            .map(GalleryPhotoResponseDto::getImageUrl)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
}
