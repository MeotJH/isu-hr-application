package com.isu.hr.health;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HealthCheckService {

    private final DataSource dataSource; // 나중에 DB 체크용

    public HealthCheckResponse getHealth() {
        return HealthCheckResponse.up();
    }

    public HealthCheckResponse getDetailedHealth() {
        Map<String, Object> details = new HashMap<>();

        // 데이터베이스 체크
        details.put("database", checkDatabase());

        // 디스크 공간 체크
        details.put("diskSpace", checkDiskSpace());

        // 메모리 체크
        details.put("memory", checkMemory());

        return HealthCheckResponse.upWithDetails(details);
    }

    private Map<String, Object> checkDatabase() {
        try {
            Connection connection = dataSource.getConnection();
            boolean isValid = connection.isValid(3); // 3초 타임아웃
            connection.close();

            return Map.of(
                    "status", isValid ? "UP" : "DOWN",
                    "database", "H2",
                    "validationQuery", "isValid()"
            );
        } catch (Exception e) {
            return Map.of(
                    "status", "DOWN",
                    "error", e.getMessage()
            );
        }
    }

    private Map<String, Object> checkDiskSpace() {
        File root = new File("/");
        long totalSpace = root.getTotalSpace();
        long freeSpace = root.getFreeSpace();
        long usedSpace = totalSpace - freeSpace;

        return Map.of(
                "total", formatBytes(totalSpace),
                "free", formatBytes(freeSpace),
                "used", formatBytes(usedSpace),
                "usagePercent", Math.round((double) usedSpace / totalSpace * 100)
        );
    }

    private Map<String, Object> checkMemory() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;

        return Map.of(
                "max", formatBytes(maxMemory),
                "total", formatBytes(totalMemory),
                "used", formatBytes(usedMemory),
                "free", formatBytes(freeMemory),
                "usagePercent", Math.round((double) usedMemory / maxMemory * 100)
        );
    }

    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
        return String.format("%.1f GB", bytes / (1024.0 * 1024.0 * 1024.0));
    }
}
