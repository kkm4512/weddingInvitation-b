package com.example.weddingInvitation_b.repository;
import com.example.weddingInvitation_b.domain.RsvpSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
/** RSVP 설정 레파지토리 */
public interface RsvpSettingRepository extends JpaRepository<RsvpSetting, Long> {
    Optional<RsvpSetting> findByMcardMcardId(Long mcardId);
}
