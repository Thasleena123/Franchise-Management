package com.example.franchisemanagement.Repository;

import com.example.franchisemanagement.entity.RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<RequestEntity,Integer> {
}
