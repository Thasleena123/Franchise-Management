package com.example.franchisemanagement.Repository;

import com.example.franchisemanagement.entity.CompanyStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyStockRepository extends JpaRepository<CompanyStockEntity, Integer> {
    Optional<CompanyStockEntity> findByProductId(int productId);
}

