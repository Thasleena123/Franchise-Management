package com.example.franchisemanagement.controller;

import com.example.franchisemanagement.Repository.SessionRepository;
import com.example.franchisemanagement.Repository.UserRepository;
import com.example.franchisemanagement.entity.OrderEntity;
import com.example.franchisemanagement.entity.SessionEntity;
import com.example.franchisemanagement.entity.UserEntity;
import com.example.franchisemanagement.enums.Role;
import com.example.franchisemanagement.sevice.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
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
        if (sessionEntity.getCreatedAt().plusSeconds(86400).isBefore(currentTime)) {
            return false;
        }
        UserEntity user = userRepository.getReferenceById(sessionEntity.getUserId());
        return user != null && user.getRole() == Role.EMPLOYEE;

    }

    @PostMapping("/create")
    public ResponseEntity<OrderEntity> createOrder(
            @RequestParam("productId") int productId,
            @RequestParam("quantitySold") int quantitySold,
            @RequestParam ("customerName")String customerName,
            @RequestHeader("Session-Id") String sessionId) {
        if (!isSessionValid(sessionId)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        try {
            SessionEntity session = sessionRepository.findBySessionId(sessionId);
            int userIdOne = session.getUserId();
            UserEntity userOne = userRepository.findUserById(userIdOne);
            OrderEntity order = employeeService.createBill(userOne.getFranchiseId(), productId, quantitySold, customerName);
            return new ResponseEntity<>(order, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/checkStock")
    public ResponseEntity<Integer> checkStockAvailability(
            @RequestParam int productId,
            @RequestHeader("Session-Id") String sessionId) {
        if (!isSessionValid(sessionId)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        try {
        SessionEntity sessionEntity = sessionRepository.findBySessionId(sessionId);

        if (sessionEntity == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        int userId = sessionEntity.getUserId();
            UserEntity user = userRepository.findFranchiseIdById(userId);

            int availableStock = employeeService.checkStockAvailability(user.getFranchiseId(), productId);
            return new ResponseEntity<>(availableStock, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}


