package com.example.franchisemanagement.entity;

import com.example.franchisemanagement.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false,length = 255)
    @NotBlank(message = "Name must not be blank")
    private  String name;

    @Column(nullable = false)
    @NotBlank(message = "Password must not be blank")
    private String password;

    @Column(nullable = false)
    @NotNull(message = "Role must not be null")
    private Role role;

    @NotNull(message = "Franchise must not be null")
    @ManyToOne
    @JoinColumn(name = "franchise_id", referencedColumnName = "franchise_id")
    private  FranchiseEntity franchise_id;

}
