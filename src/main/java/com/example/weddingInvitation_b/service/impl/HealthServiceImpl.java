package com.example.weddingInvitation_b.service.impl;

import com.example.weddingInvitation_b.repository.HealthRepository;
import com.example.weddingInvitation_b.service.HealthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 헬스 체크 서비스 구현체
 *
 * <p>HealthRepository를 통해 health 테이블에 SELECT 쿼리를 실행하여
 * DB 커넥션 활성 상태를 확인한다.
 * Aiven 무료 티어의 비활성 자동 종료를 방지하기 위해
 * /health 호출마다 실제 DB 쿼리를 발행한다.</p>
 *
 * @see HealthService
 * @see HealthRepository
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HealthServiceImpl implements HealthService {

    private final HealthRepository healthRepository;

    /**
     * DB 커넥션 연결 상태를 확인한다.
     *
     * <p>health 테이블에 COUNT(*) SELECT 쿼리를 실행하여 DB 커넥션이 정상인지 검증한다.
     * 쿼리 실행 성공 시 true, 예외 발생 시 false를 반환한다.</p>
     *
     * @return DB 연결 정상 여부 (true: 정상, false: 이상)
     */
    @Override
    @Transactional(readOnly = true)
    public boolean checkDbConnection() {
        try {
            // health 테이블에 SELECT COUNT(*) 쿼리 발행 → 실제 DB 커넥션 유지
            healthRepository.count();
            return true;
        } catch (Exception e) {
            log.error("DB 커넥션 확인 실패: {}", e.getMessage());
            return false;
        }
    }
}
