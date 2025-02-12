package com.example.franchisemanagement.Repository;

import com.example.franchisemanagement.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface OrderRepository extends JpaRepository<OrderEntity,Integer> {


}
