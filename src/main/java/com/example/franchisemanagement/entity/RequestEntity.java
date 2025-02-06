package com.example.franchisemanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Entity
@Data
public class RequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotBlank(message = "Status must not be blank")
    @Column(name = "status", nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false)
    @NotNull(message = "Product must not be null")
    private ProductEntity product_id;

    @NotNull(message = "Number of requested product must not be null")
    @Column(name = "no_of_requested_product", nullable = false)
    private int noOfRequestedProduct;
}

