package com.example.weddingInvitation_b.repository;

import com.example.weddingInvitation_b.domain.VenueTransport;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * 교통수단 안내 데이터 접근 레파지토리
 */
public interface VenueTransportRepository extends JpaRepository<VenueTransport, Long> {

    /**
     * 예식 장소 ID로 교통수단 목록 조회 (순서 오름차순)
     *
     * @param venueId 예식 장소 ID
     * @return 교통수단 목록
     */
    List<VenueTransport> findByVenueVenueIdOrderByDisplayOrderAsc(Long venueId);
}
