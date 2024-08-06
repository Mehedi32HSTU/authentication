package com.reve.authentication.server.menu;

import com.reve.authentication.server.common.Root;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "menus")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Menu extends Root {

    @NotNull(message = "Value Can't be Null")
    private Long parentMenuId;

    @NotBlank(message = "Can't be blank.")
    private String menuNameEng;

    @NotBlank(message = "Can't be blank.")
    private String menuNameBng;

    @NotBlank(message = "Can't be blank.")
    private String constantName;

    @NotBlank(message = "Can't be blank.")
    private String hyperLink;

    @NotNull(message = "Value Can't be Null")
    private Long orderIndex;

    @NotBlank(message = "Can't be blank.")
    private String icon;

    @NotBlank(message = "Can't be blank.")
    private String requestMethod;
    @NotNull(message = "Value Can't be Null")
    private Boolean isVisible;
    @NotNull(message = "Value Can't be Null")
    private Boolean isAPI;
}
