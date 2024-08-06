package com.reve.authentication.server.menu.permision;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuPermissionDTO {
    @NotNull(message = "Value Can't be Null")
    private Long roleId;
    private List<Long> menuIdList = new ArrayList<>();
}
