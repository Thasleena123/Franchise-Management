package com.example.franchisemanagement.sevice;

import com.example.franchisemanagement.Repository.*;
import com.example.franchisemanagement.entity.FranchiseEntity;
import com.example.franchisemanagement.entity.ProductEntity;
import com.example.franchisemanagement.entity.RequestEntity;
import com.example.franchisemanagement.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FranchiseAdmin {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FranchiseRepository franchiseRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private FranchiseStockRepository franchiseStockRepository;
    @Autowired
    private CompanyStockRepository companyStockRepository;

    public UserEntity createEmployeeUser(UserEntity userEntity, int franchiseId) {
        FranchiseEntity franchise = franchiseRepository.findById(franchiseId)
                .orElseThrow(() -> new RuntimeException("Franchise not found"));

        userEntity.setFranchiseId(franchiseId);
        return userRepository.save(userEntity);
    }
    public void deleteEmployeeUser(int userId) {
        userRepository.deleteById(userId);

    }
    public RequestEntity requestNewStock(int franchiseId, int productId, int quantityRequested) {
        FranchiseEntity franchise = franchiseRepository.findById(franchiseId)
                .orElseThrow(() -> new RuntimeException("Franchise not found"));

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        RequestEntity request = new RequestEntity();
        request.setStatus("Pending");
        request.setProductId(productId);
        request.setNoOfRequestedProduct(quantityRequested);
        request.setFranchiseid(franchiseId);
        return requestRepository.save(request);
    }
    }

