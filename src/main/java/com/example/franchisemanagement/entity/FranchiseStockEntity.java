package com.example.franchisemanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Entity
public class FranchiseStockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id")
    private int stockId;

    @JoinColumn(name = "franchise_id", referencedColumnName = "franchise_id", nullable = false)
    private int franchiseId;

    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private int  productId;


    @Positive
    @Column(name = "quantity")
    private int quantity;


}
