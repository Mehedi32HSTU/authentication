package com.reve.authentication.server.menu.permision;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/menu-permission")
public class MenuPermissionController {

    @Autowired
    private MenuPermissionService menuPermissionService;

    @PreAuthorize("@securityService.hasMenuPermission('/menu-permission/add')")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ResponseEntity<?> addNewMenuPermission(@Validated @RequestBody MenuPermission menuPermission) {
        return menuPermissionService.addMenuPermission(menuPermission);
    }

    @RequestMapping(value = "/{menuPermissionId}",method = RequestMethod.GET)
    public ResponseEntity<?> getMenuPermissionById(@PathVariable("menuPermissionId") Long menuPermissionId) {
        return menuPermissionService.getMenuPermissionById(menuPermissionId);
    }

    @PreAuthorize("@securityService.hasMenuPermission('/menu-permission/all')")
    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public ResponseEntity<?> getAllMenuPermission() {
        return menuPermissionService.getAllMenuPermission();
    }

    @PreAuthorize("@securityService.isSelfRole(#roleId) or @securityService.hasMenuPermission('/menu-permission/role/id')")
    @RequestMapping(value = "/role/{roleId}",method = RequestMethod.GET)
    public ResponseEntity<?> getMenuPermissionByRoleId(@PathVariable("roleId") Long roleId) {
        return menuPermissionService.getAllMenuPermissionByRole(roleId);
    }

    @PreAuthorize("@securityService.hasMenuPermission('/menu-permission/update')")
    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    public ResponseEntity<?> updateMenuPermission(@Validated @RequestBody MenuPermissionDTO menuPermissionDTO) {
        return menuPermissionService.updateMenuPermission(menuPermissionDTO);
    }

    @PreAuthorize(" @securityService.hasMenuPermission('/menu-permission/delete/id')")
    @RequestMapping(value = "/delete/{menuPermissionId}",method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMenuPermissionById(@PathVariable("menuPermissionId") Long menuPermissionId) {
        return menuPermissionService.deleteMenuPermissionById(menuPermissionId);
    }
}
