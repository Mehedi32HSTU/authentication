package com.reve.authentication.server.menu;

import com.reve.authentication.server.payload.response.MenuResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MenuServiceHelper {
    private static final Long ROOT_MENU_PARENT=0L;

    public List<MenuResponse> generateMenuForest(List<Menu> allMenus) {
        Map<Long, List<Menu>> menuMap = new HashMap<>();
        List<MenuResponse> rootResponses = new ArrayList<>();

        for (Menu menu : allMenus) {
            menuMap.computeIfAbsent(menu.getParentMenuId(), k -> new ArrayList<>()).add(menu);
        }
        for (List<Menu> menuList : menuMap.values()) {
            menuList.sort(Comparator.comparingLong(Menu::getOrderIndex));
        }

        // Build the menu response tree
        for (Menu menu : menuMap.getOrDefault(ROOT_MENU_PARENT, Collections.emptyList())) {
            rootResponses.add(buildMenuResponseTree(menu, menuMap));
        }

        return rootResponses;
    }
    private MenuResponse buildMenuResponseTree(Menu menu, Map<Long, List<Menu>> menuMap) {
        MenuResponse menuResponse = new MenuResponse();
        menuResponse.setMenu(menu);
        List<MenuResponse> childResponses = new ArrayList<>();

        for (Menu childMenu : menuMap.getOrDefault(menu.getId(), Collections.emptyList())) {
            childResponses.add(buildMenuResponseTree(childMenu, menuMap));
        }
        menuResponse.setAllChildMenus(childResponses);
        return menuResponse;
    }

}
