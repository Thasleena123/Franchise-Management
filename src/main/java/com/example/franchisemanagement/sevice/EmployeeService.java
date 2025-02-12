package com.example.franchisemanagement.sevice;

import com.example.franchisemanagement.Repository.FranchiseStockRepository;
import com.example.franchisemanagement.Repository.OrderRepository;
import com.example.franchisemanagement.Repository.ProductRepository;
import com.example.franchisemanagement.entity.FranchiseStockEntity;
import com.example.franchisemanagement.entity.OrderEntity;
import com.example.franchisemanagement.entity.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class EmployeeService {
    @Autowired
    private FranchiseStockRepository franchiseStockRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;

    public OrderEntity createBill(int franchiseId, int productId, int quantitySold, String customerName) {

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        FranchiseStockEntity franchiseStock = franchiseStockRepository.findByFranchiseIdAndProductId(franchiseId, productId)
                .orElseThrow(() -> new RuntimeException("Product not available in franchise stock"));

        if (franchiseStock.getQuantity() < quantitySold) {
            throw new RuntimeException("Insufficient stock available for the sale");
        }

        double totalPrice = product.getRetailPrice() * quantitySold;
        OrderEntity order = new OrderEntity();
        order.setFranchiseId(franchiseStock.getFranchiseId());
        order.setProductId(product.getProductId());
        order.setQuantity(quantitySold);
        order.setTotalPrice(totalPrice);
        order.setSaleDate(new Date());
        order.setCustomerName(customerName);
        order = orderRepository.save(order);

        franchiseStock.setQuantity(franchiseStock.getQuantity() - quantitySold);
        franchiseStockRepository.save(franchiseStock);

        return order;
    }
    public int checkStockAvailability(int franchiseId, int productId) {
        FranchiseStockEntity franchiseStock = franchiseStockRepository.findByFranchiseIdAndProductId(franchiseId, productId)
                .orElseThrow(() -> new RuntimeException("Product not available in franchise stock"));
        return franchiseStock.getQuantity();
    }
}
