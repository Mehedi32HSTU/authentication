package com.reve.authentication.server.menu.permision;

import org.springframework.http.ResponseEntity;

public interface MenuPermissionService {
    public ResponseEntity<?> addMenuPermission(MenuPermission menuPermission);
    public ResponseEntity<?> getMenuPermissionById(Long menuPermissionId);
    public ResponseEntity<?> getAllMenuPermission();
    public ResponseEntity<?> getAllMenuPermissionByRole(Long roleId);
    public ResponseEntity<?> updateMenuPermission(MenuPermissionDTO menuPermissionDTO);
    public ResponseEntity<?> deleteMenuPermissionById(Long menuPermissionId);
}
