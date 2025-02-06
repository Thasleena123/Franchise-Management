package com.example.franchisemanagement.Repository;

import com.example.franchisemanagement.entity.FranchiseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FranchiseRepository extends JpaRepository<FranchiseEntity,Integer> {
}
