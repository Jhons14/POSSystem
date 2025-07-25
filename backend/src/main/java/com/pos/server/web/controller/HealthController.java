package com.pos.server.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthController {

    @Autowired
    private DataSource dataSource;

    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("service", "POS System API");
        health.put("version", "1.0.0");
        
        return ResponseEntity.ok(health);
    }

    @GetMapping("/detailed")
    public ResponseEntity<Map<String, Object>> detailedHealth() {
        Map<String, Object> health = new HashMap<>();
        Map<String, Object> components = new HashMap<>();
        
        // Database health
        components.put("database", checkDatabaseHealth());
        
        // Memory health
        components.put("memory", checkMemoryHealth());
        
        // Disk space health
        components.put("diskspace", checkDiskSpaceHealth());
        
        health.put("status", components.values().stream()
                .allMatch(c -> "UP".equals(((Map<?, ?>) c).get("status"))) ? "UP" : "DOWN");
        health.put("timestamp", LocalDateTime.now());
        health.put("components", components);
        
        return ResponseEntity.ok(health);
    }

    private Map<String, Object> checkDatabaseHealth() {
        Map<String, Object> dbHealth = new HashMap<>();
        try (Connection connection = dataSource.getConnection()) {
            dbHealth.put("status", "UP");
            dbHealth.put("database", connection.getMetaData().getDatabaseProductName());
            dbHealth.put("validationQuery", connection.isValid(5));
        } catch (Exception e) {
            dbHealth.put("status", "DOWN");
            dbHealth.put("error", e.getMessage());
        }
        return dbHealth;
    }

    private Map<String, Object> checkMemoryHealth() {
        Map<String, Object> memoryHealth = new HashMap<>();
        Runtime runtime = Runtime.getRuntime();
        
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        double memoryUsagePercent = (double) usedMemory / maxMemory * 100;
        
        memoryHealth.put("status", memoryUsagePercent < 90 ? "UP" : "DOWN");
        memoryHealth.put("used", usedMemory);
        memoryHealth.put("free", freeMemory);
        memoryHealth.put("total", totalMemory);
        memoryHealth.put("max", maxMemory);
        memoryHealth.put("usagePercent", Math.round(memoryUsagePercent * 100.0) / 100.0);
        
        return memoryHealth;
    }

    private Map<String, Object> checkDiskSpaceHealth() {
        Map<String, Object> diskHealth = new HashMap<>();
        try {
            java.io.File file = new java.io.File(".");
            long totalSpace = file.getTotalSpace();
            long freeSpace = file.getFreeSpace();
            long usedSpace = totalSpace - freeSpace;
            
            double diskUsagePercent = (double) usedSpace / totalSpace * 100;
            
            diskHealth.put("status", diskUsagePercent < 85 ? "UP" : "DOWN");
            diskHealth.put("total", totalSpace);
            diskHealth.put("free", freeSpace);
            diskHealth.put("used", usedSpace);
            diskHealth.put("usagePercent", Math.round(diskUsagePercent * 100.0) / 100.0);
        } catch (Exception e) {
            diskHealth.put("status", "DOWN");
            diskHealth.put("error", e.getMessage());
        }
        return diskHealth;
    }
}