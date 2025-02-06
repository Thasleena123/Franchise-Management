package com.example.franchisemanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int product_id;

    @NotBlank(message = "Product name must not be empty")
    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "category", nullable = false)
    @NotBlank(message = "Category must not be empty")
    private String category;

    @NotNull(message = "Wholesale price must not be null")
    @Positive(message = "Wholesale price must be positive")
    @Column(name = "wholesale_price", nullable = false)
    private Double wholesalePrice;

    @NotNull(message = "Distributor price must not be null")
    @Positive(message = "Distributor price must be positive")
    @Column(name = "distributor_price", nullable = false)
    private Double distributorPrice;

    @Positive(message = "Retail price must be positive")
    @NotNull(message = "Retail price must not be null")
    @Column(name = "retail_price", nullable = false)
    private Double retailPrice;
}
