package com.zegasoftware.stock_management.controller;

import com.zegasoftware.stock_management.model.dto.user.UserSummary;
import com.zegasoftware.stock_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;

    }

    @GetMapping
    public List<UserSummary> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserSummary> getPersonById(@PathVariable UUID id) {
        return userService.getUserById(id)
                .map(e -> ResponseEntity.ok().body(e))
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
        boolean isDeleted = userService.deleteUser(id);

        if (isDeleted) {
            // If the user was successfully marked as deleted
            return ResponseEntity.ok("User has been marked as deleted.");
        } else {
            // If the user was not found or already marked as deleted
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with ID " + id + " not found or already deleted.");
        }
    }
}




