package com.example.franchisemanagement.Repository;

import com.example.franchisemanagement.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity,Integer> {
    List<OrderEntity> findByFranchiseIdAndSaleDateBetween(
            int franchiseId,
            Date startDate,
            Date endDate
    );

}
