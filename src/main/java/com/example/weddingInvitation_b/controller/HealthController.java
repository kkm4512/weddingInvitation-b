package com.example.weddingInvitation_b.controller;

import com.example.weddingInvitation_b.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 헬스 체크 컨트롤러
 *
 * <p>서버의 상태를 확인하기 위한 헬스 체크 API를 제공한다.
 * 로드 밸런서나 모니터링 시스템에서 서버 상태를 확인할 때 사용된다.</p>
 */
@RestController
@RequestMapping("/health")
@Slf4j
public class HealthController {

    /**
     * 서버 헬스 체크
     *
     * <p>서버의 기본 상태를 확인한다. 데이터베이스 연결, 메모리 상태 등을 포함할 수 있다.</p>
     *
     * @return 서버 상태 정보
     */
    @GetMapping
    public ApiResponse<Map<String, Object>> healthCheck() {
        Map<String, Object> healthData = new HashMap<>();
        healthData.put("status", "UP");
        healthData.put("timestamp", LocalDateTime.now());
        healthData.put("service", "wedding-invitation-backend");
        healthData.put("version", "1.0.0");

        log.debug("Health check requested at {}", LocalDateTime.now());

        return ApiResponse.success(healthData);
    }
}
