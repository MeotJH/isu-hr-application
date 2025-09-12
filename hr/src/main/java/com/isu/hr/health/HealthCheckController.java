package com.isu.hr.health;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
@Slf4j
public class HealthCheckController {

    private final HealthCheckService healthCheckService;

    @GetMapping
    public ResponseEntity<HealthCheckResponse> healthCheck() {
        log.info("Health check requested");
        HealthCheckResponse response = healthCheckService.getHealth();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/detailed")
    public ResponseEntity<HealthCheckResponse> detailedHealthCheck() {
        log.info("Detailed health check requested");
        try {
            HealthCheckResponse response = healthCheckService.getDetailedHealth();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Health check failed", e);
            HealthCheckResponse errorResponse = HealthCheckResponse.builder()
                    .status("DOWN")
                    .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .service("HR Management System")
                    .version("1.0.0")
                    .env("ERROR")
                    .details(Map.of("error", e.getMessage()))
                    .build();
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
        }
    }
}
