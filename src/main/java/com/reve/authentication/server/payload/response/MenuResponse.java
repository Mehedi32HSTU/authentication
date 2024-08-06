package com.reve.authentication.server.payload.response;

import com.reve.authentication.server.menu.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MenuResponse {
    private Menu menu;
    private List<MenuResponse> allChildMenus = new ArrayList<>(); // Will be empty List if it has no child menu
}
