package com.reve.authentication.server.securityImpl.controllers;

import com.reve.authentication.server.payload.request.LoginRequest;
import com.reve.authentication.server.payload.request.SignupRequest;
import com.reve.authentication.server.payload.response.JwtResponse;
import com.reve.authentication.server.securityImpl.services.AuthService;
import com.reve.authentication.server.securityImpl.services.AuthServiceImpl;
import com.reve.authentication.server.utils.Utility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Value("${authentication.cookie.access-token-name}")
    String accessTokenCookieName;

    @Value("${authentication.cookie.refresh-token-name}")
    String refreshTokenCookieName;

    @Value("${authentication.jwt.access-token-expiration-time-in-millis}")
    private Long jwtExpirationInMs;

    @Value("${authentication.jwt.refresh-token-expiration-time-in-millis}")
    private Long jwtRefreshTokenExpirationInMs;

    @Value("${authentication.jwt.refresh-token-remember-me-time-in-millis}")
    private Long jwtRefreshTokenRememberMeTimeInMs;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {

        JwtResponse jwtResponse = authService.signIn(loginRequest, request);

        ResponseCookie accessTokenCookie = authService.generateCookieFromJwtToken(accessTokenCookieName, jwtResponse.getAccessToken(), Utility.getCookiesPath(request), jwtExpirationInMs/1000);
        ResponseCookie refreshTokenCookie = authService.generateCookieFromJwtToken(refreshTokenCookieName, jwtResponse.getRefreshToken(), Utility.getCookiesPath(request), loginRequest.isRememberMeClicked() ? jwtRefreshTokenRememberMeTimeInMs/1000 : jwtRefreshTokenExpirationInMs/1000);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(jwtResponse);
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String accessToken = authService.refreshToken(request, refreshTokenCookieName);
        if (accessToken.startsWith("Error:"))
            return ResponseEntity.badRequest().body("Refresh token isn't valid! Please login again!");
        ResponseCookie accessTokenCookie = authService.generateCookieFromJwtToken(accessTokenCookieName, accessToken, Utility.getCookiesPath(request), jwtExpirationInMs/1000);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .body(accessToken);
    }
}
