package com.example.franchisemanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class FranchiseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="franchise_id")
    private int franchiseId;

    @NotBlank(message = "Location must not be empty")
    @Column(nullable = false)
    private String location;

}
