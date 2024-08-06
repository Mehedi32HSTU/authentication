package com.reve.authentication.server.user;

import com.reve.authentication.server.common.RootRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends RootRepository<User, Long> {
    public Optional<User> findByUsernameAndIsDeleted(String username, Boolean isDeleted);
    public Optional<User> findByEmailAndIsDeleted(String email, Boolean isDeleted);
    Boolean existsByUsername(String username);
    Boolean existsByUsernameAndIsDeleted(String username, Boolean isDeleted);

    Boolean existsByEmail(String email);
}
