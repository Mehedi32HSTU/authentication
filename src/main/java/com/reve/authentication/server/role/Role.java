package com.reve.authentication.server.role;

import com.reve.authentication.server.common.Root;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role extends Root {

    @NotBlank(message = "Can't be blank.")
    @Pattern(regexp = "^ROLE_[A-Z_]+$",
            message = "Role Name must start with 'ROLE_' and contain only uppercase letters and underscores")
    private String name;

    @Column
    @Size(max = 100, message = "Max limit(100) characters exceeded.")
    private String roleNameEng;
    @Column
    @Size(max = 200, message = "Max limit(200) characters exceeded.")
    private String roleNameBng;
}