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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(nullable = false,length = 255)
    @NotBlank(message = "Name must not be blank")
    private  String name;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public FranchiseEntity getFranchise_id() {
        return franchise_id;
    }

    public void setFranchise_id(FranchiseEntity franchise_id) {
        this.franchise_id = franchise_id;
    }

    @Column(nullable = false)
    @NotBlank(message = "Password must not be blank")
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(nullable = false)
    @NotNull(message = "Role must not be null")
    private Role role;

    @NotNull(message = "Franchise must not be null")
    @ManyToOne
    @JoinColumn(name = "franchise_id", referencedColumnName = "franchise_id")
    private  FranchiseEntity franchise_id;

}
