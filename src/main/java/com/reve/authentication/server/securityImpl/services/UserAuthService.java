package com.reve.authentication.server.securityImpl.services;

import com.reve.authentication.server.securityImpl.models.LoginAudit;
import com.reve.authentication.server.user.User;
import com.reve.authentication.server.payload.request.ChangePasswordRequest;
import com.reve.authentication.server.user.UserRepository;
import com.reve.authentication.server.securityImpl.security.jwt.JwtUtils;
import com.reve.authentication.server.securityImpl.security.services.AccessTokenService;
import com.reve.authentication.server.securityImpl.security.services.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AccessTokenService accessTokenService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder encoder;


    public void signOut(HttpServletRequest request, String accessTokenCookieName, String refreshTokenCookieName) {
        String accessToken = jwtUtils.parseJwt(request, accessTokenCookieName);
        String refreshToken = jwtUtils.parseJwt(request, refreshTokenCookieName);
        accessTokenService.deleteAccessToken(accessToken);
        refreshTokenService.deleteRefreshToken(refreshToken);
    }

    public void signOutFromAllDevices(HttpServletRequest request, String accessTokenCookieName) {
        String accessToken = jwtUtils.parseJwt(request, accessTokenCookieName);
        String username = jwtUtils.getUsernameFromToken(accessToken);
        accessTokenService.deleteAllAccessTokenByUsername(username);
        refreshTokenService.deleteAllRefreshTokenByUsername(username);
    }

    public String changePassword(ChangePasswordRequest changePasswordRequest, HttpServletRequest request) {
        if (!userRepository.existsByUsernameAndIsDeleted(changePasswordRequest.getUsername(), false))
            return "Error: No user found with this username!";
        User user = userRepository.findByUsernameAndIsDeleted(changePasswordRequest.getUsername(), false).get();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(changePasswordRequest.getUsername(), changePasswordRequest.getCurrentPassword()));
        if (authentication == null || !authentication.isAuthenticated()) {
            return "Error: Current password not matched!";
        }

        user.setPassword(encoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
        String username = changePasswordRequest.getUsername();
        accessTokenService.deleteAllAccessTokenByUsername(username);
        refreshTokenService.deleteAllRefreshTokenByUsername(username);
        return "Password changed successfully!";
    }

    public List<LoginAudit> getLoggedInDevices(HttpServletRequest request, String refreshTokenCookieName) {
        String refreshToken = jwtUtils.parseJwt(request, refreshTokenCookieName);
        String username = jwtUtils.getUsernameFromToken(refreshToken);
        List<LoginAudit> loginAudits = refreshTokenService.getAllRefreshTokenByUsername(username)
                .stream()
                .map(rt -> rt.getLoginAudit())
                .collect(Collectors.toList());
        return loginAudits;
    }
}
