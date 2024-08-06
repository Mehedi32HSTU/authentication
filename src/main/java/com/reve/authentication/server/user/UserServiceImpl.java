package com.reve.authentication.server.user;

import com.reve.authentication.server.common.ExceptionResponseHandler;
import com.reve.authentication.server.payload.request.SignupRequest;
import com.reve.authentication.server.payload.response.MessageResponse;
import com.reve.authentication.server.role.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService, ExceptionResponseHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserServiceHelper userServiceHelper;

    @Override
    public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {
        try {
            logger.info("registerUser Service Called.");
            if (userRepository.existsByUsernameAndIsDeleted(signUpRequest.getUsername(), false)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Username Already Exists."));
            }

            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Email Already Exists."));
            }
            User user = new User();
            user.setName(signUpRequest.getName());
            user.setUsername(signUpRequest.getUsername());
            user.setEmail(signUpRequest.getEmail());
            user.setPassword(encoder.encode(signUpRequest.getPassword()));

            Set<Role> roles = userServiceHelper.getUserRoles(signUpRequest.getRole());
            if(roles.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Roles Not Found."));
            user.setRoles(roles);
            user.setIsDeleted(false);
            User savedUser = userRepository.saveAndFlush(user);
            return ResponseEntity.status(HttpStatus.OK).body(savedUser);
        } catch (Exception e) {
            return handleException(e, "registerUser");
        }
    }

    @Override
    public ResponseEntity<?> getUserById(Long userId) {
        try {
            logger.info("getUserById Service Called.");
            User user = userRepository.findByIdAndIsDeleted(userId, false).orElse(null);
            if(Objects.isNull(user))
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User Not Found"));
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (Exception e) {
            return handleException(e, "getUserById");
        }
    }

    @Override
    public ResponseEntity<?> getUserByUserName(String username) {
        try {
            logger.info("getUserByUserName Service Called.");
            User user = userRepository.findByUsernameAndIsDeleted(username, false).orElse(null);
            if(Objects.isNull(user))
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User Not Found"));
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (Exception e) {
            return handleException(e, "getUserByUserName");
        }
    }

    @Override
    public ResponseEntity<?> getAllUser() {
        try {
            logger.info("getAllUser Service Called.");
            return ResponseEntity.status(HttpStatus.OK).body(userRepository.findAllByIsDeleted(false));
        } catch (Exception e) {
            return handleException(e, "getAllUser");
        }
    }

    @Override
    public ResponseEntity<?> updateUser(Long userId, SignupRequest newSignUpRequest) {
        try {
            logger.info("updateUser Service Called.");
            User existingUser = userRepository.findByIdAndIsDeleted(userId, false).orElse(null);

            if(Objects.isNull(existingUser))
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User Not Found"));
            User validityCheckUserName = userRepository.findByUsernameAndIsDeleted(newSignUpRequest.getUsername(), false)
                    .get();
            if(Objects.nonNull(validityCheckUserName) && !Objects.equals(existingUser.getId(), validityCheckUserName.getId()))
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Username Already Exists."));

            User validityCheckEmail = userRepository.findByEmailAndIsDeleted(newSignUpRequest.getEmail(), false)
                    .get();
            if(Objects.nonNull(validityCheckEmail) && !Objects.equals(existingUser.getId(), validityCheckEmail.getId()))
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Email Already Exists."));

            existingUser.setUsername(newSignUpRequest.getUsername());
            existingUser.setEmail(newSignUpRequest.getEmail());
            existingUser.setName(newSignUpRequest.getName());
            Set<Role> roles = userServiceHelper.getUserRoles(newSignUpRequest.getRole());
            if(roles.isEmpty())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Roles Not Found."));
            existingUser.setRoles(roles);

            return ResponseEntity.status(HttpStatus.OK).body(userRepository.saveAndFlush(existingUser));
        } catch (Exception e) {
            return handleException(e, "updateUser");
        }
    }

    @Override
    public ResponseEntity<?> deleteUserById(Long userId) {
        try {
            logger.info("deleteUserById Service Called.");
            User user = userRepository.findByIdAndIsDeleted(userId, false).orElse(null);
            if(Objects.isNull(user))
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User Not Found"));
            user.setIsDeleted(true);
            userRepository.saveAndFlush(user);
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("User Deleted"));
        } catch (Exception e) {
            return handleException(e, "deleteUserById");
        }
    }

}
