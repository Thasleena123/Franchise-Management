package com.example.franchisemanagement.controller;

import com.example.franchisemanagement.Repository.*;
import com.example.franchisemanagement.entity.*;
import com.example.franchisemanagement.enums.Role;
import com.example.franchisemanagement.sevice.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CompanyStockRepository companyStockRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private SupplyRepository supplyRepository;

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
    public ResponseEntity<FranchiseEntity> addFranchise(@RequestBody FranchiseEntity franchiseEntity, @RequestHeader("Session-Id") String sessionId) {
        if (!isSessionValid(sessionId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        FranchiseEntity savedFranchise = adminService.addFranchise(franchiseEntity);
        return ResponseEntity.ok(savedFranchise);
    }

    @DeleteMapping("/deleteFranchise/{franchiseId}")
    public ResponseEntity<String> deleteFranchise(@PathVariable int franchiseId, @RequestHeader("Session-Id") String sessionId) {

        if (!isSessionValid(sessionId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Session expired or invalid.");
        }

        adminService.deleteFranchase(franchiseId);
        return ResponseEntity.ok("Franchise deleted successfully");

    }

    @PostMapping("/createUser")
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity userEntity, @RequestHeader("Session-Id") String sessionId) {

        if (!isSessionValid(sessionId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        UserEntity savedUser = adminService.createUser(userEntity);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/addProduct")
    public ResponseEntity<ProductEntity> addProduct(@RequestBody ProductEntity product
            , @RequestHeader("Session-Id") String sessionId) {
        if (!isSessionValid(sessionId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        ProductEntity savedProduct = adminService.addProduct(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @PostMapping("/addToStock/{productId}")
    public ResponseEntity<CompanyStockEntity> addProductToStock(
            @PathVariable("productId") int productId,
            @RequestParam("quantity") int quantity,
            @RequestHeader("Session-id") String sessionId) {
        if (!isSessionValid(sessionId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        CompanyStockEntity newStock = adminService.addProductToCompanyStock(productId, quantity);

        if (newStock != null) {
            return new ResponseEntity<>(newStock, HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @PutMapping("/approveStockRequest/{requestId}")
    public ResponseEntity<String> approveStockRequest(
            @PathVariable int requestId,
            @RequestHeader("Session-id") String sessionId) {
        if (!isSessionValid(sessionId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Session expired or invalid.");
        }
        adminService.approveStocKRequest(requestId);
        return ResponseEntity.ok("Stock request approved and product allocated to franchise");
    }

    @PutMapping("/rejectStockRequest/{requestId}")
    public ResponseEntity<String> rejectStockRequest(
            @PathVariable int requestId,
            @RequestHeader("Session-id") String sessionId) {
        if (!isSessionValid(sessionId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Session expired or invalid.");
        }
        Optional<RequestEntity> requestOptional = requestRepository.findById(requestId);
        if (requestOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Stock request not found.");
        }


        RequestEntity request = requestOptional.get();
        if ("Approved".equals(request.getStatus())) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("The stock request with ID " + requestId + " has already been approved and cannot be rejected.");
        }

        adminService.rejectStockRequest(requestId);
        return ResponseEntity.ok("Stock request rejected");
    }
    @GetMapping("/company-purchases")
    public ResponseEntity<InputStreamResource> downloadCompanyPurchaseReport(
            @RequestParam Date startDate,
            @RequestParam Date endDate,
         @RequestHeader("Session-id") String sessionId)throws IOException {
        if (!isSessionValid(sessionId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        ByteArrayInputStream bis = adminService.generateCompanyPurchaseReport(startDate, endDate);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=company-purchases-report.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(new InputStreamResource(bis));
    }
}




