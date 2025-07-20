package com.pos.server.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "password_resets")
public class PasswordReset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cliente_id", nullable = false)
    private String clienteId;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(name = "expira_en", nullable = false)
    private LocalDateTime expiraEn;

    private Boolean usado;

    @Column(name = "ip_solicitud")
    private InetAddress ipSolicitud;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    // Relación
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", insertable = false, updatable = false)
    private Cliente cliente;

    // Constructores
    public PasswordReset() {
        this.fechaCreacion = LocalDateTime.now();
        this.usado = false;
    }

    public PasswordReset(String clienteId, InetAddress ipSolicitud) {
        this();
        this.clienteId = clienteId;
        this.token = "reset_" + UUID.randomUUID().toString();
        this.expiraEn = LocalDateTime.now().plusHours(1); // Expira en 1 hora
        this.ipSolicitud = ipSolicitud;
    }

    // Métodos de utilidad
    public boolean isExpirado() {
        return LocalDateTime.now().isAfter(this.expiraEn);
    }

    public boolean isValido() {
        return !usado && !isExpirado();
    }

    public void marcarComoUsado() {
        this.usado = true;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiraEn() {
        return expiraEn;
    }

    public void setExpiraEn(LocalDateTime expiraEn) {
        this.expiraEn = expiraEn;
    }

    public Boolean getUsado() {
        return usado;
    }

    public void setUsado(Boolean usado) {
        this.usado = usado;
    }

    public InetAddress getIpSolicitud() {
        return ipSolicitud;
    }

    public void setIpSolicitud(InetAddress ipSolicitud) {
        this.ipSolicitud = ipSolicitud;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return "PasswordReset{" +
                "id=" + id +
                ", clienteId='" + clienteId + '\'' +
                ", token='" + token + '\'' +
                ", expiraEn=" + expiraEn +
                ", usado=" + usado +
                '}';
    }
}