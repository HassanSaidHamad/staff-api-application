package com.staff.application.controller.auth;

import com.staff.application.model.User;
import com.staff.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User newUser = userService.register(user.getName(), user.getUsername(), user.getPassword());
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

}
