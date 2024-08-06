package com.reve.authentication.server.role;

import org.springframework.http.ResponseEntity;

public interface RoleService {
    public ResponseEntity<?> addRole(Role role);
    public ResponseEntity<?> getRoleById(Long roleId);
    public ResponseEntity<?> getAllRole();
    public ResponseEntity<?> updateRole(Long roleId, Role newRoleReq);
    public ResponseEntity<?> deleteRoleById(Long roleId);
}
