package com.example.weddingInvitation_b.service;

/**
 * 헬스 체크 서비스 인터페이스
 *
 * <p>서버 상태 및 DB 연결 상태를 확인하는 비즈니스 로직을 정의한다.</p>
 *
 * @see com.example.weddingInvitation_b.service.impl.HealthServiceImpl
 */
public interface HealthService {

    /**
     * DB 커넥션 연결 상태를 확인한다.
     *
     * <p>health 테이블에 SELECT 쿼리를 실행하여 DB 커넥션이 정상인지 검증한다.
     * 쿼리 실행 성공 시 true, 예외 발생 시 false를 반환한다.</p>
     *
     * @return DB 연결 정상 여부 (true: 정상, false: 이상)
     */
    boolean checkDbConnection();
}
