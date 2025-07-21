package com.pos.server.domain.model;

import com.pos.server.infrastructure.persistence.entity.Compra;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Customer {

    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private String email;
    private String username;
    private String password;

    private LocalDateTime registrationDate;
    private LocalDateTime lastUpdated;
    private LocalDateTime lastLogin;
    private Integer loginAttempts;
    private Boolean active;
    private Boolean emailVerified;
    private Boolean accountLocked;
    private String profilePicture;
    private LocalDate birthDate;
    private String gender;
    // getters y setters...

    // Constructor sin parámetros - Sistema
    public Customer() {
        this.registrationDate = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
        this.loginAttempts = 0;
        this.active = true;
        this.emailVerified = false;
        this.accountLocked = false;
    }

    // Constructor con parámetros - Solo datos del usuario
    public Customer(String firstName, String lastName, String email, String username, String password) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;  // ← ESENCIAL

    }

    public Customer(String firstName, String lastName, String phone, String address, String email, String username, LocalDate birthDate, String gender, String password) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.username = username;
        this.birthDate = birthDate;
        this.gender = gender;
        this.password = password;  // ← ESENCIAL
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Integer getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(Integer loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public Boolean getAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(Boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


}
