package com.reve.authentication.server.securityImpl.security.services;

import com.reve.authentication.server.common.ExceptionResponseHandler;
import com.reve.authentication.server.menu.Menu;
import com.reve.authentication.server.menu.MenuRepository;
import com.reve.authentication.server.menu.permision.MenuPermissionRepository;
import com.reve.authentication.server.role.ERole;
import com.reve.authentication.server.role.Role;
import com.reve.authentication.server.role.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class SecurityService implements ExceptionResponseHandler {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MenuPermissionRepository menuPermissionRepository;

    @Autowired
    private MenuRepository menuRepository;

    public UserDetailsImpl getLoggedInUser() {
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    public Long getCurrentLoggedInUserId() {
        try {
            UserDetailsImpl userDetails = getLoggedInUser();
            return userDetails.getId();
        } catch (Exception e) {
            handleException(e, "getCurrentLoggedInUserId");
        }
        return 0L;
    }
    public boolean hasMenuPermission(String requestPath) {
        // TODO: Have to optimise here.
        if(hasExpectedRole(ERole.ROLE_SUPER_ADMIN.getRole())) return true;
        if(requestPath.isBlank()) return false;

        Menu menu = menuRepository.findByHyperLinkAndIsDeleted(requestPath, false);
        if(Objects.nonNull(menu)) {
            Set<Long> allPermittedRoles = menuPermissionRepository
                    .findAllRoleIdByMenuIdAndIsDeleted(menu.getId(), false);
            List<Role> allRoles = getAccessibleRoles();
            for (Role role : allRoles) {
                if(allPermittedRoles.contains(role.getId())) return true;
            }
        }
        return false;
    }
    public boolean hasMenuPermissionWithEntity() {
        return true;
    }
    public boolean isSelfRole(Long roleId) {
        return getAccessibleRoles().stream().anyMatch(role -> Objects.equals(role.getId(), roleId));
    }
    public boolean isSelfByUsername(String username) {
        UserDetailsImpl userDetails = this.getLoggedInUser();
        return hasExpectedRole(ERole.ROLE_SUPER_ADMIN.getRole()) || Objects.equals(userDetails.getUsername(), username);
    }
    public boolean isSelfByUserId(Long userId) {
        UserDetailsImpl userDetails = this.getLoggedInUser();
        return hasExpectedRole(ERole.ROLE_SUPER_ADMIN.getRole()) || Objects.equals(userDetails.getId(), userId);
    }
    private boolean hasExpectedRole(String roleName) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            return userDetails.getAuthorities().stream()
                    .anyMatch(authority -> Objects.equals(roleName, authority.getAuthority()));
        } catch (Exception e) {
            handleException(e, "hasExpectedRole");
        }
        return false;
    }

    private List<Role> getAccessibleRoles() {
        List<Role> allRoles = new ArrayList<>();
        try {
            // TODO: Have to optimise here, have to fetch from cache or static map
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                String roleName = authority.getAuthority();
                Role role = roleRepository.findByName(roleName).orElse(null);
                if(Objects.nonNull(role)) allRoles.add(role);
            }
        } catch (Exception e) {
            handleException(e, "getAccessibleRoles");
        }
        return allRoles;
    }

}
