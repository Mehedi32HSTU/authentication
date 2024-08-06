package com.reve.authentication.server.menu.permision;

import com.reve.authentication.server.common.Root;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "menu_permission")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MenuPermission extends Root {
    private Long menuId;
    private Long roleId;
}
