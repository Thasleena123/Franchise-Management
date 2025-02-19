package com.example.franchisemanagement.controller;

import com.example.franchisemanagement.Repository.SessionRepository;
import com.example.franchisemanagement.Repository.UserRepository;
import com.example.franchisemanagement.entity.RequestEntity;
import com.example.franchisemanagement.entity.SessionEntity;
import com.example.franchisemanagement.entity.UserEntity;
import com.example.franchisemanagement.enums.Role;
import com.example.franchisemanagement.sevice.FranchiseAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.sql.Date;
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

    @PostMapping("/createEmployee")
    public ResponseEntity<UserEntity> createEmployee(@RequestBody UserEntity userEntity,
                                                     @RequestHeader("Session-Id") String sessionId) {
        if (!isSessionValid(sessionId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        try {
            UserEntity savedUser = franchiseAdminService.createEmployeeUser(userEntity);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteEmployee/{userId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("userId") int userId,
                                               @RequestHeader("Session-Id") String sessionId) {
        if (!isSessionValid(sessionId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        try {
            franchiseAdminService.deleteEmployeeUser(userId);
            return new ResponseEntity<>("Successfully Deleted",HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/request-new-stock/{productId}")
    public ResponseEntity<RequestEntity> requestNewStock(
                                                         @PathVariable("productId") int productId,
                                                         @RequestParam("quantity") int quantityRequested,
                                                         @RequestHeader("Session-Id") String sessionId) {
        if (!isSessionValid(sessionId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        try {
            RequestEntity newRequest = franchiseAdminService.requestNewStock( productId, quantityRequested, sessionId);
            return new ResponseEntity<>(newRequest, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/franchise-sales/{franchiseId}")
    public ResponseEntity<InputStreamResource> downloadFranchiseSalesReport(
            @PathVariable int franchiseId,
            @RequestParam Date startDate,
            @RequestParam Date endDate) {

        ByteArrayInputStream bis = franchiseAdminService.generateFranchiseSalesReport(franchiseId, startDate, endDate);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=franchise-sales-report.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(new InputStreamResource(bis));
    }
}



