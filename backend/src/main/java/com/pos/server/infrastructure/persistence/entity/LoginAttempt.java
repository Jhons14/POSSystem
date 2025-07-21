package com.pos.server.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.net.InetAddress;
import java.time.LocalDateTime;

@Entity
@Table(name = "login_attempts")
public class LoginAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cliente_id")
    private Long clienteId;

    @Column(name = "ip_address")
    private InetAddress ipAddress;

    private Boolean exitoso;

    @Column(name = "razon_fallo")
    private String razonFallo;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "fecha_intento")
    private LocalDateTime fechaIntento;

    // Relación
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", insertable = false, updatable = false)
    private Cliente cliente;

    // Constructores
    public LoginAttempt() {
        this.fechaIntento = LocalDateTime.now();
    }

    public LoginAttempt(Long clienteId, InetAddress ipAddress, Boolean exitoso, String razonFallo, String userAgent) {
        this();
        this.clienteId = clienteId;
        this.ipAddress = ipAddress;
        this.exitoso = exitoso;
        this.razonFallo = razonFallo;
        this.userAgent = userAgent;
    }

    // Métodos estáticos para crear intentos comunes
    public static LoginAttempt exitoso(Long clienteId, InetAddress ipAddress, String userAgent) {
        return new LoginAttempt(clienteId, ipAddress, true, null, userAgent);
    }

    public static LoginAttempt fallido(Long clienteId, InetAddress ipAddress, String razon, String userAgent) {
        return new LoginAttempt(clienteId, ipAddress, false, razon, userAgent);
    }

    public static LoginAttempt passwordIncorrecto(Long clienteId, InetAddress ipAddress, String userAgent) {
        return fallido(clienteId, ipAddress, "password_incorrecto", userAgent);
    }

    public static LoginAttempt usuarioBloqueado(Long clienteId, InetAddress ipAddress, String userAgent) {
        return fallido(clienteId, ipAddress, "usuario_bloqueado", userAgent);
    }

    public static LoginAttempt usuarioNoEncontrado(InetAddress ipAddress, String userAgent) {
        return fallido(null, ipAddress, "usuario_no_encontrado", userAgent);
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Boolean getExitoso() {
        return exitoso;
    }

    public void setExitoso(Boolean exitoso) {
        this.exitoso = exitoso;
    }

    public String getRazonFallo() {
        return razonFallo;
    }

    public void setRazonFallo(String razonFallo) {
        this.razonFallo = razonFallo;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public LocalDateTime getFechaIntento() {
        return fechaIntento;
    }

    public void setFechaIntento(LocalDateTime fechaIntento) {
        this.fechaIntento = fechaIntento;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return "LoginAttempt{" +
                "id=" + id +
                ", clienteId='" + clienteId + '\'' +
                ", exitoso=" + exitoso +
                ", razonFallo='" + razonFallo + '\'' +
                ", fechaIntento=" + fechaIntento +
                '}';
    }
}
