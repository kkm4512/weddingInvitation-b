package com.example.weddingInvitation_b.repository;
import com.example.weddingInvitation_b.domain.GuestbookSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
/** 방명록 설정 레파지토리 */
public interface GuestbookSettingRepository extends JpaRepository<GuestbookSetting, Long> {
    Optional<GuestbookSetting> findByMcardMcardId(Long mcardId);
}
