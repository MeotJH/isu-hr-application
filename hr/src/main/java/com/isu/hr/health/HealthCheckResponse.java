package com.isu.hr.health;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HealthCheckResponse {
    private String status;
    private String timestamp;
    private String service;
    private String version;
    private String env;
    private Map<String, Object> details;

    public static HealthCheckResponse up() {
        return HealthCheckResponse.builder()
                .status("UP")
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .service("HR Management System")
                .version("1.0.0")
                .env(getEnvironment())
                .build();
    }

    public static HealthCheckResponse upWithDetails(Map<String, Object> details) {
        return up().toBuilder()
                .details(details)
                .build();
    }

    private static String getEnvironment() {
        String profile = System.getProperty("spring.profiles.active", "local");
        return profile.toUpperCase();
    }
}
