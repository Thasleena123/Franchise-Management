package com.example.franchisemanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyPurchaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int purchaseId;

    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private int productId;

    private Date purchaseDate;

    private int quantity;

    public CompanyPurchaseEntity(int productId, Date date, int quantity) {
        this.productId = productId;
        this.purchaseDate = date;
        this.quantity = quantity;
    }
}
