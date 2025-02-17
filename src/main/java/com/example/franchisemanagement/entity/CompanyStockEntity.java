package com.example.franchisemanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
@Entity
public class CompanyStockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id")
    private int stockId;


    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private  int productId;

    @PositiveOrZero
    @Column(name = "quantity")
    private int quantity;

}
