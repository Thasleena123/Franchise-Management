package com.example.franchisemanagement.Repository;

import com.example.franchisemanagement.entity.SupplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
    public interface SupplyRepository extends JpaRepository<SupplyEntity, Long> {

    }


