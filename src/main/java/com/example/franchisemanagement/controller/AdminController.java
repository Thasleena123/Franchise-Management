package com.example.franchisemanagement.controller;

import com.example.franchisemanagement.Repository.SessionRepository;
import com.example.franchisemanagement.Repository.UserRepository;
import com.example.franchisemanagement.entity.FranchiseEntity;
import com.example.franchisemanagement.entity.SessionEntity;
import com.example.franchisemanagement.entity.UserEntity;
import com.example.franchisemanagement.enums.Role;
import com.example.franchisemanagement.sevice.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionRepository sessionRepository;

    private boolean isSessionValid(String sessionId) {
        SessionEntity sessionEntity = sessionRepository.findBySessionId(sessionId);
        if (sessionEntity == null) {
            return false;
        }

        LocalDateTime currentTime = LocalDateTime.now();
        if (sessionEntity.getCreatedAt().plusSeconds(86400).isBefore(currentTime)) {
            return false;
        }
        Optional<UserEntity> user = userRepository.findById(sessionEntity.getUserId());
        return user.get() != null && user.get().getRole() == Role.ADMIN;

    }

        @PostMapping("/addFranchise")
        public ResponseEntity<FranchiseEntity> addFranchise(@RequestBody FranchiseEntity franchiseEntity,@RequestHeader("Session-Id") String sessionId)
       {
           if (!isSessionValid(sessionId)){
               return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
           }

        FranchiseEntity savedFranchise = adminService.addFranchise(franchiseEntity);
            return ResponseEntity.ok(savedFranchise);
        }

        @DeleteMapping("/deleteFranchise/{franchiseId}")
        public ResponseEntity<String> deleteFranchise(@PathVariable int franchiseId,  @RequestHeader("Session-Id") String sessionId) {

                if (!isSessionValid(sessionId)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Session expired or invalid.");
                }

                adminService.deleteFranchase(franchiseId);
                return ResponseEntity.ok("Franchise deleted successfully");

        }
        @PostMapping("/createUser")
        public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity userEntity,@RequestHeader("Session-Id") String sessionId) {

            if (!isSessionValid(sessionId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
            UserEntity savedUser = adminService.createUser(userEntity);
            return ResponseEntity.ok(savedUser);
        }
        @PutMapping("/approveStockRequest/{requestId}")
        public ResponseEntity<String> approveStockRequest(
                @PathVariable int requestId,
                @RequestParam int adminId,
                @RequestHeader("Session-id") String sessionId) {
            if (!isSessionValid(sessionId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Session expired or invalid.");
            }
            adminService.approveStocKRequest(requestId, adminId);
            return ResponseEntity.ok("Stock request approved and product allocated to franchise");
        }
        @PutMapping("/rejectStockRequest/{requestId}")
        public ResponseEntity<String> rejectStockRequest(
                @PathVariable int requestId,
                @RequestParam int adminId,@RequestHeader("Session-id") String sessionId) {
            if (!isSessionValid(sessionId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Session expired or invalid.");
            }
            adminService.rejectStockRequest(requestId, adminId);
            return ResponseEntity.ok("Stock request rejected");
        }

}
