package com.example.weddingInvitation_b.controller;

import com.example.weddingInvitation_b.dto.response.ApiResponse;
import com.example.weddingInvitation_b.service.HealthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 헬스 체크 컨트롤러
 *
 * <p>서버의 상태를 확인하기 위한 헬스 체크 API를 제공한다.
 * 로드 밸런서나 모니터링 시스템에서 서버 상태를 확인할 때 사용된다.
 * serverIp 필드를 통해 도커 컨테이너 IP를 확인할 수 있어 LB 분산 여부 검증에 활용된다.
 * DB 커넥션 유지를 위해 매 호출마다 실제 SELECT 쿼리를 발행한다.</p>
 *
 * @see HealthService
 */
@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
@Slf4j
public class HealthController {

    private final HealthService healthService;

    /**
     * 서버 헬스 체크
     *
     * <p>서버의 기본 상태와 현재 컨테이너(서버) IP 주소를 반환한다.
     * DB 연결 상태도 함께 확인하여 dbStatus 필드로 반환한다.
     * serverIp는 로드밸런서 구성 시 요청이 서로 다른 서버로 분산되는지 확인하는 데 사용된다.</p>
     *
     * @return 서버 상태 정보 (status, dbStatus, timestamp, service, version, serverIp, hostname)
     */
    @GetMapping
    public ApiResponse<Map<String, Object>> healthCheck() {
        // DB 커넥션 확인 (실제 SELECT 쿼리 발행 → Aiven 비활성 자동 종료 방지)
        boolean dbConnected = healthService.checkDbConnection();

        Map<String, Object> map = new HashMap<>();
        map.put("status", "UP");
        map.put("dbStatus", dbConnected ? "UP" : "DOWN");
        map.put("timestamp", LocalDateTime.now());
        map.put("service", "wedding-invitation-backend");
        map.put("version", "0.0.1");
        map.put("serverIp", resolveServerIp());
        map.put("hostname", resolveHostname());

        log.debug("Health check requested at {}", LocalDateTime.now());

        return ApiResponse.success(map);
    }

    /**
     * 현재 서버(또는 도커 컨테이너)의 IP 주소를 조회한다.
     *
     * <p>네트워크 인터페이스를 순회하여 루프백(127.x)과 링크-로컬(169.254.x)을 제외한
     * 첫 번째 사이트-로컬 또는 글로벌 IPv4 주소를 반환한다.
     * 조회 실패 시 "unknown"을 반환한다.</p>
     *
     * @return 서버 IP 문자열 (예: "172.17.0.3"), 조회 실패 시 "unknown"
     */
    private String resolveServerIp() {
        try {
            // 네트워크 인터페이스 전체 순회하여 루프백·링크-로컬 제외한 첫 IPv4 주소 반환
            for (NetworkInterface ni : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (!ni.isUp() || ni.isLoopback() || ni.isVirtual()) continue;
                for (InetAddress addr : Collections.list(ni.getInetAddresses())) {
                    // IPv4 & 루프백 아님 & 링크-로컬 아님
                    if (addr.getAddress().length == 4
                            && !addr.isLoopbackAddress()
                            && !addr.isLinkLocalAddress()) {
                        return addr.getHostAddress();
                    }
                }
            }
            // 인터페이스 탐색 실패 시 InetAddress 기본 조회 사용
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            log.warn("Failed to resolve server IP: {}", e.getMessage());
            return "unknown";
        }
    }

    /**
     * 현재 서버(또는 도커 컨테이너)의 호스트명을 조회한다.
     *
     * @return 호스트명 문자열, 조회 실패 시 "unknown"
     */
    private String resolveHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            log.warn("Failed to resolve hostname: {}", e.getMessage());
            return "unknown";
        }
    }
}
