package com.pos.server.domain.model;

import java.net.InetAddress;
import java.time.LocalDateTime;

public class Session {
    private Integer sessionId;
    private String customerId;
    private String sessionToken;
    private InetAddress ipAddress;
    private String userAgent;
    private LocalDateTime expiresAt;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime lastUsed;
    // getters y setters...


    public Session(Integer sessionId, String customerId, String sessionToken, InetAddress ipAddress, String userAgent, LocalDateTime expiresAt, Boolean active, LocalDateTime createdAt, LocalDateTime lastUsed) {
        this.sessionId = sessionId;
        this.customerId = customerId;
        this.sessionToken = sessionToken;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.expiresAt = expiresAt;
        this.active = active;
        this.createdAt = createdAt;
        this.lastUsed = lastUsed;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(LocalDateTime lastUsed) {
        this.lastUsed = lastUsed;
    }
}
