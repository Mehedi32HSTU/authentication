package com.reve.authentication.server.securityImpl.services;

import com.reve.authentication.server.securityImpl.security.services.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminAuthService {

    @Autowired
    private RefreshTokenService refreshTokenService;

    public String revokeUserAccessByUsername(String username) {
        refreshTokenService.revokeUserAccessByUsername(username);
        return "Revoked all access for this user: " + username;
    }
}
