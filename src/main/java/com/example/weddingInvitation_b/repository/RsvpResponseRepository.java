package com.example.weddingInvitation_b.repository;
import com.example.weddingInvitation_b.domain.RsvpResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
/** RSVP 응답 레파지토리 */
public interface RsvpResponseRepository extends JpaRepository<RsvpResponse, Long> {
    List<RsvpResponse> findByMcardMcardIdOrderByRespondedAtDesc(Long mcardId);
}
