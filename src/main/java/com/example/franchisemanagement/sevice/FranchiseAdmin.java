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
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private Authenticate authenticate;

public UserEntity createEmployeeUser(UserEntity userEntity) {
    userEntity.setPassword(authenticate.encodePassword(userEntity.getPassword()));
    return userRepository.save(userEntity);
}

    public void deleteEmployeeUser(int userId) {
        userRepository.deleteById(userId);

    }
    public RequestEntity requestNewStock( int productId, int quantityRequested, String sessionId) {
//        FranchiseEntity franchise = franchiseRepository.findById(franchiseId)
//                .orElseThrow(() -> new RuntimeException("Franchise not found"));
//        RequestEntity request= requestRepository. findByProductId(productId)
//                .orElseThrow(()-> new RuntimeException("product is not available "));

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        RequestEntity request = new RequestEntity();
        request.setStatus("Pending");
        request.setProductId(productId);
        request.setNoOfRequestedProduct(quantityRequested);
        int userId = sessionRepository.findBySessionId(sessionId).getUserId();
        UserEntity user = userRepository.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("User Not Found");
        }
        request.setFranchiseid(user.getFranchiseId());
        return requestRepository.save(request);
    }
    }

