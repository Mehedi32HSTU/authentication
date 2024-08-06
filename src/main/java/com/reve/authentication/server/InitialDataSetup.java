package com.reve.authentication.server;

import com.reve.authentication.server.role.ERole;
import com.reve.authentication.server.role.Role;
import com.reve.authentication.server.user.User;
import com.reve.authentication.server.role.RoleRepository;
import com.reve.authentication.server.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;

@Component
public class InitialDataSetup implements CommandLineRunner {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${com.reve.authentication.super.admin.name}")
    private String superAdminName;

    @Value("${com.reve.authentication.super.admin.username}")
    private String superAdminUsername;

    @Value("${com.reve.authentication.super.admin.password}")
    private String superAdminPassword;

    @Value("${com.reve.authentication.super.admin.email}")
    private String superAdminEmail;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) {

        if(!roleRepository.existsBy()) {
            try {
                Role roleSuperAdmin = new Role();
                roleSuperAdmin.setName(ERole.ROLE_SUPER_ADMIN.getRole());
                roleSuperAdmin.setRoleNameBng("সুপার এডমিন");
                roleSuperAdmin.setRoleNameEng("Super Admin");
                /*
                roleSuperAdmin.setCreatedBy(0L);
                roleSuperAdmin.setCreationDate(System.currentTimeMillis());
                roleSuperAdmin.setLastModifiedBy(0L);
                roleSuperAdmin.setLastModificationDate(System.currentTimeMillis());
                */
                roleSuperAdmin.setIsDeleted(false);

                roleRepository.save(roleSuperAdmin);

                Role superAdminRole = roleRepository.findByName(ERole.ROLE_SUPER_ADMIN.getRole()).orElseThrow(null);
                if(Objects.nonNull(superAdminRole)) {
                    User superAdmin = new User();
                    superAdmin.setName(superAdminName);
                    superAdmin.setUsername(superAdminUsername);
                    superAdmin.setEmail(superAdminEmail);
                    superAdmin.setPassword(passwordEncoder.encode(superAdminPassword));
                    superAdmin.setRoles(Collections.singleton(superAdminRole));
                    superAdmin.setIsDeleted(false);

                    userRepository.save(superAdmin);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Exception {} has occurred while creating Admin using runner class.", e);
            }
        }
        preLoadData();
    }
    private void preLoadData() {
        // TODO: 1. Load all role.
        //  2. Load all Menu.
        //  3. Load all Menu Permissions.
    }

}
