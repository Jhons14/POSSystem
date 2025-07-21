package com.pos.server.domain.model;

import java.net.InetAddress;
import java.time.LocalDateTime;

public class PasswordResetRequest {
    private Long requestId;
    private Long customerId;
    private String resetToken;
    private LocalDateTime expiresAt;
    private Boolean used;
    private InetAddress requestIp;
    private LocalDateTime createdAt;
    // getters y setters...

    public PasswordResetRequest(Long requestId, Long customerId, String resetToken, LocalDateTime expiresAt, Boolean used, InetAddress requestIp, LocalDateTime createdAt) {
        this.requestId = requestId;
        this.customerId = customerId;
        this.resetToken = resetToken;
        this.expiresAt = expiresAt;
        this.used = used;
        this.requestIp = requestIp;
        this.createdAt = createdAt;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    public InetAddress getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(InetAddress requestIp) {
        this.requestIp = requestIp;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
