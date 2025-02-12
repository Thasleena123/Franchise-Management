package com.example.franchisemanagement.controller;

import com.example.franchisemanagement.entity.UserEntity;
import com.example.franchisemanagement.sevice.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserEntity user, HttpServletRequest request, HttpServletResponse response) {
        return userService.loginUser(user.getName(), user.getPassword(), request, response);
    }
//
//    @PostMapping("/register")
//    public ResponseEntity<String> registerUser(@RequestBody UserEntity user, @RequestParam String adminName) {
//        return userService.registerUser(user, adminName);
//    }
}

