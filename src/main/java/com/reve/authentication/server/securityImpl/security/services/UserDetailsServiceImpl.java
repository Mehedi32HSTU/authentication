package com.reve.authentication.server.securityImpl.security.services;

import com.reve.authentication.server.user.User;
import com.reve.authentication.server.user.UserRepository;
import com.reve.authentication.server.user.UserService;
import com.reve.authentication.server.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameAndIsDeleted(username, false).get();
        if (Objects.isNull(user))
            throw new UsernameNotFoundException("Username not found!");
        return UserDetailsImpl.build(user);
    }
}
