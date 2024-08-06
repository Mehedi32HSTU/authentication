package com.reve.authentication.server.user;

import com.reve.authentication.server.payload.request.SignupRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {

    public ResponseEntity<?> registerUser(SignupRequest signUpRequest);
    public ResponseEntity<?> getUserById(Long userId);
    public ResponseEntity<?> getUserByUserName(String username);
    public ResponseEntity<?> getAllUser();
    public ResponseEntity<?> updateUser(Long userId, SignupRequest newSignUpRequest);
    public ResponseEntity<?> deleteUserById(Long userId);

}
