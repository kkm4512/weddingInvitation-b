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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

    // 병렬 DB 조회용 스레드 풀
    // max-pool-size(15)와 맞춤 — 커넥션 대기 없이 처리 가능한 수준
    private static final ExecutorService PARALLEL_EXECUTOR = Executors.newFixedThreadPool(15);

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

        // 18개 섹션 쿼리를 병렬로 동시 실행 (순차 실행 대비 ~10배 빠름)
        CompletableFuture<Object> coupleF       = safeAsync(() -> mcardCoupleService.getCouple(mcardId));
        CompletableFuture<Object> greetingF     = safeAsync(() -> mcardGreetingService.getGreeting(mcardId));
        CompletableFuture<Object> scheduleF     = safeAsync(() -> mcardScheduleService.getSchedule(mcardId));
        CompletableFuture<Object> venueF        = safeAsync(() -> mcardVenueService.getVenue(mcardId));
        CompletableFuture<Object> quoteF        = safeAsync(() -> mcardQuoteService.getQuote(mcardId));
        CompletableFuture<Object> videoF        = safeAsync(() -> mcardVideoService.getVideo(mcardId));
        CompletableFuture<Object> wreathF       = safeAsync(() -> mcardWreathService.getWreath(mcardId));
        CompletableFuture<Object> photoQuoteF   = safeAsync(() -> mcardPhotoQuoteService.getPhotoQuote(mcardId));
        CompletableFuture<Object> bgmF          = safeAsync(() -> mcardBgmService.getBgm(mcardId));
        CompletableFuture<Object> noticesF      = safeAsync(() -> mcardNoticeService.getNotices(mcardId));
        CompletableFuture<Object> galleryF      = safeAsync(() -> getGalleryImages(mcardId));
        CompletableFuture<Object> accountsF     = safeAsync(() -> bankAccountService.getAccounts(mcardId));
        CompletableFuture<Object> contactsF     = safeAsync(() -> mcardContactService.getContacts(mcardId));
        CompletableFuture<Object> rsvpSettingF  = safeAsync(() -> rsvpService.getSetting(mcardId));
        CompletableFuture<Object> gbSettingF    = safeAsync(() -> guestbookService.getSetting(mcardId));
        CompletableFuture<Object> gbMessagesF   = safeAsync(() -> guestbookService.getMessages(mcardId));
        CompletableFuture<Object> sectionOrderF = safeAsync(() -> mcardSectionOrderService.getSectionOrder(mcardId));

        // 전체 완료 대기
        CompletableFuture.allOf(
            coupleF, greetingF, scheduleF, venueF, quoteF, videoF,
            wreathF, photoQuoteF, bgmF, noticesF, galleryF,
            accountsF, contactsF, rsvpSettingF, gbSettingF, gbMessagesF, sectionOrderF
        ).join();

        // 결과 조립
        McardSectionOrderResponseDto sectionOrderDto = (McardSectionOrderResponseDto) sectionOrderF.join();
        List<String> sectionOrder = sectionOrderDto != null ? sectionOrderDto.getSectionOrder() : List.of();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("couple",            coupleF.join());
        response.put("greeting",          greetingF.join());
        response.put("schedule",          translateSchedule((McardScheduleResponseDto) scheduleF.join()));
        response.put("venue",             translateVenue((McardVenueResponseDto) venueF.join()));
        response.put("quote",             quoteF.join());
        response.put("video",             videoF.join());
        response.put("wreath",            wreathF.join());
        response.put("photoQuote",        photoQuoteF.join());
        response.put("bgm",               bgmF.join());
        response.put("notices",           noticesF.join());
        response.put("gallery",           galleryF.join());
        response.put("accounts",          accountsF.join());
        response.put("contacts",          contactsF.join());
        response.put("rsvpSettings",      rsvpSettingF.join());
        response.put("guestbookSettings", gbSettingF.join());
        response.put("guestbookMessages", gbMessagesF.join());
        response.put("mcardId",           mcardId);
        response.put("title",             mcard.getTitle());
        response.put("inviteCode",        mcard.getInviteCode());
        response.put("hasWatermark",      mcard.getHasWatermark());
        response.put("createdAt",         mcard.getCreatedAt());
        response.put("updatedAt",         mcard.getUpdatedAt());
        response.put("sectionOrder",      sectionOrder);

        return ApiResponse.success(response);
    }

    private <T> T safe(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception ex) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private <T> CompletableFuture<Object> safeAsync(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(() -> (Object) safe(supplier), PARALLEL_EXECUTOR);
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
