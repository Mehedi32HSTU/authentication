package com.reve.authentication.server.user;

import com.reve.authentication.server.role.Role;
import com.reve.authentication.server.role.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserServiceHelper {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RoleRepository roleRepository;
    public Set<Role> getUserRoles(Set<String> rolesName) {
        Set<Role> roles = new HashSet<>();
        for (String roleName : rolesName) {
            Role role = roleRepository.findByNameIgnoreCaseAndIsDeleted(roleName, false);
            if (Objects.nonNull(role)) {
                roles.add(role);
            }
        }
        return roles;
    }
}
