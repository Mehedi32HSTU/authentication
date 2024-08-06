package com.reve.authentication.server.securityImpl.services;

import com.reve.authentication.server.menu.Menu;
import com.reve.authentication.server.payload.request.LoginRequest;
import com.reve.authentication.server.payload.request.SignupRequest;
import com.reve.authentication.server.payload.response.JwtResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    public JwtResponse signIn(LoginRequest loginRequest, HttpServletRequest request);
    public ResponseCookie generateCookieFromJwtToken(String accessTokenCookieName, String accessToken, String path, long expirationInSec);
    public String refreshToken(HttpServletRequest request, String refreshTokenCookieName);
}
