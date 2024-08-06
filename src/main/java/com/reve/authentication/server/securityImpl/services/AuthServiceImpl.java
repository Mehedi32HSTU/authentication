package com.reve.authentication.server.securityImpl.services;

import com.reve.authentication.server.common.ExceptionResponseHandler;
import com.reve.authentication.server.payload.response.MessageResponse;
import com.reve.authentication.server.role.Role;
import com.reve.authentication.server.securityImpl.models.*;
import com.reve.authentication.server.payload.request.LoginRequest;
import com.reve.authentication.server.payload.request.SignupRequest;
import com.reve.authentication.server.payload.response.JwtResponse;
import com.reve.authentication.server.role.RoleRepository;
import com.reve.authentication.server.user.UserRepository;
import com.reve.authentication.server.securityImpl.security.jwt.JwtUtils;
import com.reve.authentication.server.securityImpl.security.jwt.TokenType;
import com.reve.authentication.server.securityImpl.security.services.AccessTokenService;
import com.reve.authentication.server.securityImpl.security.services.RefreshTokenService;
import com.reve.authentication.server.securityImpl.security.services.UserDetailsImpl;
import com.reve.authentication.server.user.User;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService, ExceptionResponseHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private AccessTokenService accessTokenService;

    @Override
    public JwtResponse signIn (LoginRequest loginRequest, HttpServletRequest request) {
        logger.info("signIn Service Called.");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication, TokenType.ACCESS_TOKEN, false);
        accessTokenService.storeAccessTokenInRedis(
                AccessToken.builder()
                        .username(loginRequest.getUsername())
                        .token(jwt)
                        .isRevoked(false)
                        .build()
        );

        String refreshToken = jwtUtils.generateJwtToken(authentication, TokenType.REFRESH_TOKEN, loginRequest.isRememberMeClicked());

        refreshTokenService.storeRefreshTokenInRedis(
                refreshTokenService.addLoginAuditToRefreshToken(RefreshToken
                        .builder()
                        .username(loginRequest.getUsername())
                        .token(refreshToken)
                        .isBlacklisted(false)
                        .isInvalidated(false)
                        .isRevoked(false)
                        .build(), request),
                loginRequest.isRememberMeClicked()
        );

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new JwtResponse(
                jwt,
                refreshToken,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles
        );
    }

    @Override
    public ResponseCookie generateCookieFromJwtToken(String accessTokenCookieName, String accessToken, String path, long expirationInSec) {
        logger.info("generateCookieFromJwtToken Service Called.");
        return jwtUtils.generateCookieFromJwtToken( accessTokenCookieName, accessToken, path, expirationInSec );
    }

    @Override
    public String refreshToken(HttpServletRequest request, String refreshTokenCookieName) {
        String refreshToken = jwtUtils.parseJwt(request, refreshTokenCookieName);
        if (!refreshTokenService.validateRefreshToken(refreshToken))
            return "Error: Refresh token isn't valid! Please login again!";
        String accessToken = refreshTokenService.createNewAccessToken(refreshToken);
        accessTokenService.storeAccessTokenInRedis(
                AccessToken.builder()
                        .token(accessToken)
                        .username(jwtUtils.getUsernameFromToken(accessToken))
                        .isRevoked(false)
                        .build()
        );
        return accessToken;
    }
}
