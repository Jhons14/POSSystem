package com.pos.server.domain;

import java.net.InetAddress;
import java.time.LocalDateTime;

public class LoginAttemptRecord {
    private Integer attemptId;
    private String customerId;
    private InetAddress ipAddress;
    private Boolean successful;
    private String failureReason;
    private String userAgent;
    private LocalDateTime attemptTime;
    // getters y setters...

    public LoginAttemptRecord(Integer attemptId, String customerId, InetAddress ipAddress, Boolean successful, String failureReason, String userAgent, LocalDateTime attemptTime) {
        this.attemptId = attemptId;
        this.customerId = customerId;
        this.ipAddress = ipAddress;
        this.successful = successful;
        this.failureReason = failureReason;
        this.userAgent = userAgent;
        this.attemptTime = attemptTime;
    }

    public Integer getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(Integer attemptId) {
        this.attemptId = attemptId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Boolean getSuccessful() {
        return successful;
    }

    public void setSuccessful(Boolean successful) {
        this.successful = successful;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public LocalDateTime getAttemptTime() {
        return attemptTime;
    }

    public void setAttemptTime(LocalDateTime attemptTime) {
        this.attemptTime = attemptTime;
    }
}
