package com.example.franchisemanagement.controller;

import com.example.franchisemanagement.Repository.SessionRepository;
import com.example.franchisemanagement.Repository.UserRepository;
import com.example.franchisemanagement.entity.RequestEntity;
import com.example.franchisemanagement.entity.SessionEntity;
import com.example.franchisemanagement.entity.UserEntity;
import com.example.franchisemanagement.enums.Role;
import com.example.franchisemanagement.sevice.FranchiseAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("franchiseAdmin")
public class FranchiseAdminController {
    @Autowired
    private FranchiseAdmin franchiseAdminService;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UserRepository userRepository;

    private boolean isSessionValid(String sessionId) {
        SessionEntity sessionEntity = sessionRepository.findBySessionId(sessionId);
        if (sessionEntity == null) {
            return false;
        }
        LocalDateTime currentTime = LocalDateTime.now();
        if (sessionEntity.getCreatedAt().plusSeconds(86400).isBefore(currentTime))
        {
            return false;
        }
        UserEntity user = userRepository.getReferenceById(sessionEntity.getUserId());
        return user != null && user.getRole() == Role.FRANCHISE_OWNER;


    }

    @PostMapping("/createEmployee/{franchiseId}")
    public ResponseEntity<UserEntity> createEmployee(@RequestBody UserEntity userEntity,
                                                     @PathVariable("franchiseId") int franchiseId,
                                                     @RequestHeader("Session-Id") String sessionId) {
        if (!isSessionValid(sessionId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        try {
            UserEntity savedUser = franchiseAdminService.createEmployeeUser(userEntity, franchiseId);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteEmployee/{userId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("userId") int userId,
                                               @RequestHeader("Session-Id") String sessionId) {
        if (!isSessionValid(sessionId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        try {
            franchiseAdminService.deleteEmployeeUser(userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/request-new-stock/{franchiseId}/{productId}")
    public ResponseEntity<RequestEntity> requestNewStock(@PathVariable int franchiseId,
                                                         @PathVariable int productId,
                                                         @RequestParam int quantityRequested,
                                                         @RequestHeader("Session-Id") String sessionId) {
        if (!isSessionValid(sessionId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        try {
            RequestEntity newRequest = franchiseAdminService.requestNewStock(franchiseId, productId, quantityRequested);
            return new ResponseEntity<>(newRequest, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}


