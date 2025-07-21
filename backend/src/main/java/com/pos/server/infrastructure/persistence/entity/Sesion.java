package com.pos.server.infrastructure.persistence.entity;


import jakarta.persistence.*;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.UUID;

// ================================================================
// ENTIDAD PARA SESIONES
// ================================================================
    @Entity
    @Table(name = "sesiones")
    public class Sesion {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(name = "cliente_id", nullable = false)
        private Long clienteId;

        @Column(name = "token_sesion", nullable = false, unique = true)
        private String tokenSesion;

        @Column(name = "ip_address")
        private InetAddress ipAddress;

        @Column(name = "user_agent")
        private String userAgent;

        @Column(name = "expira_en", nullable = false)
        private LocalDateTime expiraEn;

        private Boolean activa;

        @Column(name = "fecha_creacion")
        private LocalDateTime fechaCreacion;

        @Column(name = "fecha_ultimo_uso")
        private LocalDateTime fechaUltimoUso;

        // Relación
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "cliente_id", insertable = false, updatable = false)
        private Cliente cliente;

        // Constructores
        public Sesion() {
            this.fechaCreacion = LocalDateTime.now();
            this.fechaUltimoUso = LocalDateTime.now();
            this.activa = true;
        }

        public Sesion(Long clienteId, InetAddress ipAddress, String userAgent) {
            this();
            this.clienteId = clienteId;
            this.tokenSesion = "session_" + UUID.randomUUID().toString();
            this.expiraEn = LocalDateTime.now().plusDays(7); // Expira en 7 días
            this.ipAddress = ipAddress;
            this.userAgent = userAgent;
        }

        // Métodos de utilidad
        public boolean isExpirada() {
            return LocalDateTime.now().isAfter(this.expiraEn);
        }

        public boolean isValida() {
            return activa && !isExpirada();
        }

        public void actualizarUltimoUso() {
            this.fechaUltimoUso = LocalDateTime.now();
        }

        public void extenderSesion(int dias) {
            this.expiraEn = LocalDateTime.now().plusDays(dias);
            actualizarUltimoUso();
        }

        public void cerrarSesion() {
            this.activa = false;
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

        public String getTokenSesion() {
            return tokenSesion;
        }

        public void setTokenSesion(String tokenSesion) {
            this.tokenSesion = tokenSesion;
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

        public LocalDateTime getExpiraEn() {
            return expiraEn;
        }

        public void setExpiraEn(LocalDateTime expiraEn) {
            this.expiraEn = expiraEn;
        }

        public Boolean getActiva() {
            return activa;
        }

        public void setActiva(Boolean activa) {
            this.activa = activa;
        }

        public LocalDateTime getFechaCreacion() {
            return fechaCreacion;
        }

        public void setFechaCreacion(LocalDateTime fechaCreacion) {
            this.fechaCreacion = fechaCreacion;
        }

        public LocalDateTime getFechaUltimoUso() {
            return fechaUltimoUso;
        }

        public void setFechaUltimoUso(LocalDateTime fechaUltimoUso) {
            this.fechaUltimoUso = fechaUltimoUso;
        }

        public Cliente getCliente() {
            return cliente;
        }

        public void setCliente(Cliente cliente) {
            this.cliente = cliente;
        }

        @Override
        public String toString() {
            return "Sesion{" +
                    "id=" + id +
                    ", clienteId='" + clienteId + '\'' +
                    ", tokenSesion='" + tokenSesion + '\'' +
                    ", activa=" + activa +
                    ", expiraEn=" + expiraEn +
                    '}';
        }
}

