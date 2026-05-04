package com.example.weddingInvitation_b.repository;

import com.example.weddingInvitation_b.domain.GalleryPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

/**
 * 갤러리 사진 데이터 접근 레파지토리
 */
public interface GalleryPhotoRepository extends JpaRepository<GalleryPhoto, Long> {
    List<GalleryPhoto> findByMcardMcardIdOrderByDisplayOrderAsc(Long mcardId);

    /**
     * 현재 갤러리의 최대 순서 값 조회 (다음 순서 자동 부여용)
     */
    @Query("SELECT MAX(g.displayOrder) FROM GalleryPhoto g WHERE g.mcard.mcardId = :mcardId")
    Optional<Integer> findMaxDisplayOrderByMcardId(Long mcardId);
}
