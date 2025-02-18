package com.example.franchisemanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;

@Entity
@Data
public class SupplyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int  supplyId;

    private Date dateOfSupply;

    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private  int productId;

    private int quantity;

    @JoinColumn(name = "franchise_id", referencedColumnName = "franchise_id", nullable = false)
    private  int franchiseId;

    public SupplyEntity(Date dateOfSupply, int productId, int quantity, int franchiseId) {
        this.dateOfSupply = dateOfSupply;
        this.productId = productId;
        this.quantity = quantity;
        this.franchiseId = franchiseId;
    }
}
