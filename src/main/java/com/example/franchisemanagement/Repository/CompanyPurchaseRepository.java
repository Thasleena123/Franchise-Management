package com.example.franchisemanagement.Repository;

import com.example.franchisemanagement.entity.CompanyPurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface CompanyPurchaseRepository extends JpaRepository<CompanyPurchaseEntity, Integer>  {
    List<CompanyPurchaseEntity> findByPurchaseDateBetween(Date startDate, Date endDate);
}
