package com.reve.authentication.server.menu;

import org.springframework.http.ResponseEntity;

public interface MenuService {
    public ResponseEntity<?> addMenu(Menu menu);
    public ResponseEntity<?> getMenuById(Long menuId);
    public ResponseEntity<?> getAllMenu();
    public ResponseEntity<?> getAllMenuByRole(Long roleId);
    public ResponseEntity<?> updateMenu(Long menuId, Menu newMenuReq);
    public ResponseEntity<?> deleteMenuById(Long menuId);
}
