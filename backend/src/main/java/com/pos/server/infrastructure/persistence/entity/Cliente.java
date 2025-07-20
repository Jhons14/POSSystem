package com.pos.server.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;




@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ← Auto-incremental
    @Column(name = "id")
    private Long id;

    private String nombre;
    private String apellidos;
    private String celular;
    private String direccion;

    @Column(name = "correo_electronico", unique = true, nullable = false)
    private String correoElectronico;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    // Campos de control
    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(name = "ultimo_login")
    private LocalDateTime ultimoLogin;

    @Column(name = "intentos_login")
    private Integer intentosLogin;

    // Estados del usuario
    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "email_verificado")
    private Boolean emailVerificado;

    @Column(name = "cuenta_bloqueada")
    private Boolean cuentaBloqueada;

    // Verificación por email
    @Column(name = "token_verificacion")
    private String tokenVerificacion;

    @Column(name = "token_verificacion_expira")
    private LocalDateTime tokenVerificacionExpira;

    // Campos adicionales
    @Column(name = "foto_perfil")
    private String fotoPerfil;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    private String genero;

    // Relaciones
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Compra> compras;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PasswordReset> passwordResets;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Sesion> sesiones;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LoginAttempt> loginAttempts;

    // Constructores
    // Constructor sin parámetros - Sistema
    public Cliente() {
        this.fechaRegistro = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
        this.intentosLogin = 0;
        this.activo = true;
        this.emailVerificado = false;
        this.cuentaBloqueada = false;
    }

    // Constructor básico - Solo datos esenciales del usuario
    public Cliente(String nombre, String apellidos, String correoElectronico, String username, String passwordHash) {
        this();
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correoElectronico = correoElectronico;
        this.username = username;
        this.passwordHash = passwordHash;  // ← ESENCIAL
    }

    // Constructor completo - Con datos opcionales del usuario
    public Cliente(String nombre, String apellidos, String celular, String direccion, String correoElectronico, String username, LocalDate fechaNacimiento, String genero, String passwordHash) {
        this();
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.celular = celular;
        this.direccion = direccion;
        this.correoElectronico = correoElectronico;
        this.username = username;
        this.passwordHash = passwordHash;  // ← ESENCIAL
        this.fechaNacimiento = fechaNacimiento;
        this.genero = genero;
    }
    // Métodos de actualización automática
    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (this.fechaRegistro == null) {
            this.fechaRegistro = LocalDateTime.now();
        }
        this.fechaActualizacion = LocalDateTime.now();
    }

    // Getters y Setters
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public LocalDateTime getUltimoLogin() {
        return ultimoLogin;
    }

    public void setUltimoLogin(LocalDateTime ultimoLogin) {
        this.ultimoLogin = ultimoLogin;
    }

    public Integer getIntentosLogin() {
        return intentosLogin;
    }

    public void setIntentosLogin(Integer intentosLogin) {
        this.intentosLogin = intentosLogin;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Boolean getEmailVerificado() {
        return emailVerificado;
    }

    public void setEmailVerificado(Boolean emailVerificado) {
        this.emailVerificado = emailVerificado;
    }

    public Boolean getCuentaBloqueada() {
        return cuentaBloqueada;
    }

    public void setCuentaBloqueada(Boolean cuentaBloqueada) {
        this.cuentaBloqueada = cuentaBloqueada;
    }

    public String getTokenVerificacion() {
        return tokenVerificacion;
    }

    public void setTokenVerificacion(String tokenVerificacion) {
        this.tokenVerificacion = tokenVerificacion;
    }

    public LocalDateTime getTokenVerificacionExpira() {
        return tokenVerificacionExpira;
    }

    public void setTokenVerificacionExpira(LocalDateTime tokenVerificacionExpira) {
        this.tokenVerificacionExpira = tokenVerificacionExpira;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public List<Compra> getCompras() {
        return compras;
    }

    public void setCompras(List<Compra> compras) {
        this.compras = compras;
    }

    public List<PasswordReset> getPasswordResets() {
        return passwordResets;
    }

    public void setPasswordResets(List<PasswordReset> passwordResets) {
        this.passwordResets = passwordResets;
    }

    public List<Sesion> getSesiones() {
        return sesiones;
    }

    public void setSesiones(List<Sesion> sesiones) {
        this.sesiones = sesiones;
    }

    public List<LoginAttempt> getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(List<LoginAttempt> loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    // Métodos de utilidad
    public String getNombreCompleto() {
        if (nombre != null && apellidos != null) {
            return nombre + " " + apellidos;
        } else if (nombre != null) {
            return nombre;
        } else if (apellidos != null) {
            return apellidos;
        }
        return username;
    }

    public boolean isActivo() {
        return activo != null && activo;
    }

    public boolean isEmailVerificado() {
        return emailVerificado != null && emailVerificado;
    }

    public boolean isCuentaBloqueada() {
        return cuentaBloqueada != null && cuentaBloqueada;
    }

    public boolean puedeIniciarSesion() {
        return isActivo() && !isCuentaBloqueada();
    }

    public void incrementarIntentosLogin() {
        this.intentosLogin = (this.intentosLogin == null ? 0 : this.intentosLogin) + 1;
    }

    public void resetearIntentosLogin() {
        this.intentosLogin = 0;
    }

    public void actualizarUltimoLogin() {
        this.ultimoLogin = LocalDateTime.now();
        this.resetearIntentosLogin();
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", username='" + username + '\'' +
                ", correoElectronico='" + correoElectronico + '\'' +
                ", activo=" + activo +
                '}';
    }
}