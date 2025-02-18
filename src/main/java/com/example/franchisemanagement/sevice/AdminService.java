package com.example.franchisemanagement.sevice;

import com.example.franchisemanagement.Repository.*;
import com.example.franchisemanagement.entity.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class AdminService {
    @Autowired
    private FranchiseStockRepository franchiseStockRepository;
    @Autowired
    private FranchiseRepository franchiseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private CompanyStockRepository companyStockRepository;
    @Autowired
    private Authenticate authenticate;


    public FranchiseEntity addFranchise(FranchiseEntity franchiseEntity) {
        return franchiseRepository.save(franchiseEntity);
    }

    public void deleteFranchase(int franchiseId) {
        franchiseRepository.deleteById(franchiseId);
    }

    public UserEntity createUser(UserEntity userEntity) {
        userEntity.setPassword(authenticate.encodePassword(userEntity.getPassword()));
        return userRepository.save(userEntity);
    }
    public ProductEntity addProduct(ProductEntity product) {
        return productRepository.save(product); // Save the product to the database
    }
    public ProductEntity updateProduct(int productId,ProductEntity updatedProduct){
        ProductEntity product = productRepository.findByproductId(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setProductName(updatedProduct.getProductName());
        product.setCategory(updatedProduct.getCategory());
        product.setDistributorPrice(updatedProduct.getDistributorPrice());
        product.setRetailPrice(updatedProduct.getRetailPrice());
        product.setWholesalePrice(updatedProduct.getWholesalePrice());

        return productRepository.save(product);
    }
    @Transactional
    public CompanyStockEntity addProductToCompanyStock(int productId, int quantity) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CompanyStockEntity companyStock = new CompanyStockEntity();
        companyStock.setProductId(productId);
        companyStock.setQuantity(quantity);
        return companyStockRepository.save(companyStock);
    }


    @Transactional
    public void approveStocKRequest(int RequestId) {
        RequestEntity request = requestRepository.findById(RequestId)
                .orElseThrow(() -> new RuntimeException("stock request not found"));

        request.setStatus("Approved");
        requestRepository.save(request);
        allocateProductToFranchase(request.getFranchiseid(), request.getProductId(), request.getNoOfRequestedProduct(), request.getRequestId());
    }

    @Transactional
    public void rejectStockRequest(int requestId) {
        RequestEntity request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Stock request not found"));
        if("Approved".equals(request.getStatus())){
            throw  new RuntimeException("request is already approved so cannot be rejected");
        }
        request.setStatus("Rejected");
        requestRepository.save(request);
    }
    public void allocateProductToFranchase(int franchiseId, int  productId, int quantityToAllocate, int requestId) {
        CompanyStockEntity companyStockEntity = companyStockRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Product not found in company stock"));

        if (companyStockEntity.getQuantity() < quantityToAllocate) {
            throw new RuntimeException("not enough stock in company inventory");
        }
        FranchiseStockEntity franchiseStock = franchiseStockRepository.findByFranchiseIdAndProductId(franchiseId, productId)
                .orElse(null);
        if (franchiseStock != null) {
            franchiseStock.setQuantity(franchiseStock.getQuantity() + quantityToAllocate);
            franchiseStockRepository.save(franchiseStock);
        } else {
            franchiseStock = new FranchiseStockEntity();
            franchiseStock.setFranchiseId(franchiseId);
            franchiseStock.setProductId(productId);
            franchiseStock.setQuantity(quantityToAllocate);

            franchiseStockRepository.save(franchiseStock);

        }
        companyStockEntity.setQuantity(companyStockEntity.getQuantity() - quantityToAllocate);
        companyStockRepository.save(companyStockEntity);

    }

//    public Byte[] generateCompanyReport() {
//
//    }
}




