package com.example.franchisemanagement.Repository;

import com.example.franchisemanagement.entity.FranchiseStockEntity;
import com.example.franchisemanagement.entity.RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<RequestEntity,Integer> {
    Optional<RequestEntity> findById(int requestId);
    List<RequestEntity> findByStatus(String status);

    Optional<RequestEntity> findByProductId(int productId);
}

