package com.example.franchisemanagement.Repository;

import com.example.franchisemanagement.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity,Integer> {
    Optional<ProductEntity>findByproductId(int productId);
}
