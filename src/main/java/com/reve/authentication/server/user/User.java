package com.reve.authentication.server.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reve.authentication.server.common.Root;
import com.reve.authentication.server.role.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "users")
@Data
public class User extends Root {

    @Column
    @Size(max = 100, message = "Max limit(100) characters exceeded.")
    private String name;

    @Column
    @NotBlank(message = "Can't be blank.")
    @Size(max = 30, message = "Max limit(30) characters exceeded.")
    private String username;

    @Column
    @NotBlank(message = "Can't be blank.")
    @Size(max = 70, message = "Max limit(70) characters exceeded.")
    @Email(message = "Email pattern should be matched.")
    private String email;

    @Column
    @NotBlank(message = "Can't be blank.")
    @Size(max = 120, message = "Max limit(120) characters exceeded.")
    @JsonIgnore
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(  name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
