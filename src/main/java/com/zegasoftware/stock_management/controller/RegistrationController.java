package com.zegasoftware.stock_management.controller;

import com.zegasoftware.stock_management.model.dto.user.UserDetails;
import com.zegasoftware.stock_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public String saveUser(@RequestBody UserDetails userDetails) {
        if (userService.saveUser(userDetails)) {
            return "User " + userDetails.getUsername() + " registered!";
        }else {
            return "Registration failed";
        }
    }
}
