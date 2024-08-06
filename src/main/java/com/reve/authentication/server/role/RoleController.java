package com.reve.authentication.server.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PreAuthorize("@securityService.hasMenuPermission('/role/add')")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ResponseEntity<?> addNewRole(@Validated @RequestBody Role role) {
        return roleService.addRole(role);
    }

    @RequestMapping(value = "/{roleId}",method = RequestMethod.GET)
    public ResponseEntity<?> getRoleById(@PathVariable("roleId") Long roleId) {
        return roleService.getRoleById(roleId);
    }

    @PreAuthorize("@securityService.hasMenuPermission('/role/all')")
    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public ResponseEntity<?> getAllRole() {
        return roleService.getAllRole();
    }

    @PreAuthorize("@securityService.hasMenuPermission('/role/update/id')")
    @RequestMapping(value = "/update/{roleId}",method = RequestMethod.PUT)
    public ResponseEntity<?> updateRole(@PathVariable("roleId") Long roleId, @Validated @RequestBody Role newRoleReq) {
        return roleService.updateRole(roleId,newRoleReq);
    }

    @PreAuthorize("@securityService.hasMenuPermission('/role/delete/id')")
    @RequestMapping(value = "/delete/{roleId}",method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteRoleById(@PathVariable("roleId") Long roleId) {
        return roleService.deleteRoleById(roleId);
    }

}
