package com.example.franchisemanagement.entity;

import jakarta.persistence.*;
import lombok.Data;


import java.sql.Date;

@Data
@Entity
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "franchise_id", nullable = false)
    private int franchiseId;

    @JoinColumn(name = "product_id", nullable = false)
    private int productId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double totalPrice;

    @Column(nullable = false)
    private Date saleDate;

    @Column(nullable = false)
    private String customerName;

}


