package com.example.franchisemanagement.sevice;

import com.example.franchisemanagement.Repository.UserRepository;
import com.example.franchisemanagement.entity.UserEntity;
import com.example.franchisemanagement.enums.Role;
import com.example.franchisemanagement.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // 🔹 Register a User (ADMIN can create Franchise Owners & Employees)
    public ResponseEntity<String> registerUser(UserEntity user, String adminName) {
        Optional<UserEntity> adminUser = Optional.ofNullable(userRepository.findByUsername(adminName));

        if (adminUser.isPresent() && adminUser.get().getRole() == Role.ADMIN) {
            // 🔹 Encode password before saving
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return ResponseEntity.ok("User registered successfully");
        }
        return ResponseEntity.status(403).body("Only ADMIN can register new users");
    }

    // 🔹 Login a User
    public ResponseEntity<String> loginUser(String name, String password, HttpServletResponse response) {
        Optional<UserEntity> user = Optional.ofNullable(userRepository.findByUsername(name));

        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            String token = jwtUtil.generateToken(name);

            Cookie cookie = new Cookie("jwt", token);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(86400); // 1 day
            response.addCookie(cookie);

            return ResponseEntity.ok("Login successful");
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }
}




