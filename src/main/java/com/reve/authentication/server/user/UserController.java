package com.reve.authentication.server.user;

import com.reve.authentication.server.menu.Menu;
import com.reve.authentication.server.payload.request.SignupRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PreAuthorize("@securityService.hasMenuPermission('/user/register')")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return userService.registerUser(signUpRequest);
    }

    @PreAuthorize("@securityService.isSelfByUserId(#userId) or @securityService.hasMenuPermission('/user/id')")
    @RequestMapping(value = "/{userId}",method = RequestMethod.GET)
    public ResponseEntity<?> getUserById(@PathVariable("userId") Long userId) {
        return userService.getUserById(userId);
    }

    @PreAuthorize("@securityService.isSelfByUsername(#username) or @securityService.hasMenuPermission('/user/username')")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getUserByUsername(@RequestParam(name = "username", required = true) String username) {
        return userService.getUserByUserName(username);
    }

    @PreAuthorize("@securityService.hasMenuPermission('/user/all')")
    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public ResponseEntity<?> getAllUser() {
        return userService.getAllUser();
    }

    @PreAuthorize("@securityService.hasMenuPermission('/user/update/id')")
    @RequestMapping(value = "/update/{userId}",method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@PathVariable("userId") Long userId, @Validated @RequestBody SignupRequest newSignUpRequest) {
        return userService.updateUser(userId,newSignUpRequest);
    }

    @PreAuthorize("@securityService.hasMenuPermission('/user/delete/id')")
    @RequestMapping(value = "/delete/{userId}",method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUserById(@PathVariable("userId") Long userId) {
        return userService.deleteUserById(userId);
    }


}
