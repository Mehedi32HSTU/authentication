package com.reve.authentication.server.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @PreAuthorize("@securityService.hasMenuPermission('/menu/add')")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ResponseEntity<?> addNewMenu(@Validated @RequestBody Menu menu) {
        return menuService.addMenu(menu);
    }

    @RequestMapping(value = "/{menuId}",method = RequestMethod.GET)
    public ResponseEntity<?> getMenuById(@PathVariable("menuId") Long menuId) {
        return menuService.getMenuById(menuId);
    }

    @PreAuthorize("@securityService.hasMenuPermission('/menu/all')")
    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public ResponseEntity<?> getAllMenu() {
        return menuService.getAllMenu();
    }

    @PreAuthorize("@securityService.isSelfRole(#roleId) or @securityService.hasMenuPermission('/menu/role/id')")
    @RequestMapping(value = "/role/{roleId}",method = RequestMethod.GET)
    public ResponseEntity<?> getMenuByRoleId(@PathVariable("roleId") Long roleId) {
        return menuService.getAllMenuByRole(roleId);
    }

    @PreAuthorize("@securityService.hasMenuPermission('/menu/update/id')")
    @RequestMapping(value = "/update/{menuId}",method = RequestMethod.PUT)
    public ResponseEntity<?> updateMenu(@PathVariable("menuId") Long menuId, @Validated @RequestBody Menu neuMenuReq) {
        return menuService.updateMenu(menuId,neuMenuReq);
    }

    @PreAuthorize("@securityService.hasMenuPermission('/menu/delete/id')")
    @RequestMapping(value = "/delete/{menuId}",method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMenuById(@PathVariable("menuId") Long menuId) {
        return menuService.deleteMenuById(menuId);
    }
}
