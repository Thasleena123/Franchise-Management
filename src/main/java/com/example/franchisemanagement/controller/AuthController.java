package com.example.franchisemanagement.controller;

import com.example.franchisemanagement.entity.UserEntity;
import com.example.franchisemanagement.sevice.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public class AuthController {
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    private final UserService userService;

    // 🔹 Login User (Sets JWT in Cookie)
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserEntity user, HttpServletResponse response) {
        return userService.loginUser(user.getName(), user.getPassword(), response);
    }

    // 🔹 Register a new Franchise Owner or Employee (Only Admin)
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserEntity user, @RequestParam String adminName) {
        return userService.registerUser(user, adminName);
    }
}

