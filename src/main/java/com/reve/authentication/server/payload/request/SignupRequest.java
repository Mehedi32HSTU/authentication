package com.reve.authentication.server.payload.request;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.validation.constraints.*;

public class SignupRequest {
    @NotBlank(message = "Can't be blank.")
    @Size(min = 3, max = 100, message = "Limit 3-100 characters.")
    private String name;
    @NotBlank(message = "Can't be blank.")
    @Size(min = 3, max = 30,message = "Limit 3-30 characters.")
    private String username;

    @NotBlank(message = "Can't be blank.")
    @Size(max = 70,message = "Max limit(70) characters exceeded.")
    @Email(message = "Email pattern should be matched.")
    private String email;

    private Set<String> role;

    @NotBlank(message = "Can't be blank.")
    @Size(min = 6, max = 120,message = "Limit 3-120 characters.")
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRole() {
        return Objects.isNull(this.role) ? new HashSet<>() : this.role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }
}
