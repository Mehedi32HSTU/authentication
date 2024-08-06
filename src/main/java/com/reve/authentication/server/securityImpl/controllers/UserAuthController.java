package com.reve.authentication.server.securityImpl.controllers;

import com.reve.authentication.server.securityImpl.models.LoginAudit;
import com.reve.authentication.server.payload.request.ChangePasswordRequest;
import com.reve.authentication.server.securityImpl.services.AuthServiceImpl;
import com.reve.authentication.server.securityImpl.services.UserAuthService;
import com.reve.authentication.server.utils.Utility;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserAuthController {

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private UserAuthService userAuthService;

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

    @PostMapping("/sign-out")
    public ResponseEntity<?> signOut(HttpServletRequest request) {
        userAuthService.signOut(request, accessTokenCookieName, refreshTokenCookieName);
        ResponseCookie accessTokenCookie = authService.generateCookieFromJwtToken(accessTokenCookieName, null, Utility.getCookiesPath(request), 0);
        ResponseCookie refreshTokenCookie = authService.generateCookieFromJwtToken(refreshTokenCookieName, null, Utility.getCookiesPath(request), 0);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body("Successfully signed out from the system!");
    }

    @PostMapping("/sign-out-from-all-device")
    public ResponseEntity<?> signOutFromAllDevice(HttpServletRequest request) {
        userAuthService.signOutFromAllDevices(request, accessTokenCookieName);
        ResponseCookie accessTokenCookie = authService.generateCookieFromJwtToken(accessTokenCookieName, null, Utility.getCookiesPath(request), 0);
        ResponseCookie refreshTokenCookie = authService.generateCookieFromJwtToken(refreshTokenCookieName, null, Utility.getCookiesPath(request), 0);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body("Successfully signed out from the system!");
    }

    @PostMapping("/change-password")
    @PreAuthorize("hasRole('SUPER_ADMIN') or @securityService.isSelfByUsername(#changePasswordRequest.username)")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest, HttpServletRequest request) {
        String message = userAuthService.changePassword(changePasswordRequest, request);
        if (message.startsWith("Error:"))
            return ResponseEntity.badRequest().body(message);
        ResponseCookie accessTokenCookie = authService.generateCookieFromJwtToken(accessTokenCookieName, null, Utility.getCookiesPath(request), 0);
        ResponseCookie refreshTokenCookie = authService.generateCookieFromJwtToken(refreshTokenCookieName, null, Utility.getCookiesPath(request), 0);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body("Password changed successfully!");
    }

    @GetMapping("/get-logged-in-devices")
    public ResponseEntity<?> getAllLoggedInDevices(HttpServletRequest request) {
        List<LoginAudit> loginAudits = userAuthService.getLoggedInDevices(request, refreshTokenCookieName);
        return ResponseEntity.ok().body(loginAudits);
    }
}
