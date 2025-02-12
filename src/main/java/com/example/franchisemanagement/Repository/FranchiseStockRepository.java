package com.example.franchisemanagement.Repository;

import com.example.franchisemanagement.entity.FranchiseStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FranchiseStockRepository extends JpaRepository<FranchiseStockEntity, Integer> {
    Optional<FranchiseStockEntity> findByFranchiseIdAndProductId(int franchiseId, int productId);
}
