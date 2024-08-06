package com.reve.authentication.server.role;

import com.reve.authentication.server.common.RootRepository;

import java.util.Optional;

public interface RoleRepository extends RootRepository<Role, Long> {
    Optional<Role> findByName(String name);
    Role findByNameIgnoreCaseAndIsDeleted(String name, Boolean isDeleted);
}
