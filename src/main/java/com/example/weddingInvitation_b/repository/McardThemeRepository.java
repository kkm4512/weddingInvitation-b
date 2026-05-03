package com.example.weddingInvitation_b.repository;

import com.example.weddingInvitation_b.domain.McardTheme;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 청첩장 테마 레파지토리
 */
public interface McardThemeRepository extends JpaRepository<McardTheme, Long> {
    McardTheme findByMcard_McardId(Long mcardId);
}
