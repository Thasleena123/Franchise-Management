package com.example.franchisemanagement.controller;

import com.example.franchisemanagement.Repository.FranchiseRepository;
import com.example.franchisemanagement.entity.FranchiseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {

    private final FranchiseRepository franchiseRepository;

    @Autowired
    public Test(FranchiseRepository franchiseRepository) {
        this.franchiseRepository = franchiseRepository;
    }

    @PostMapping("franchise")
    public FranchiseEntity save(@RequestBody FranchiseEntity franchise) {
        System.out.println("here");
        FranchiseEntity result = franchiseRepository.save(franchise);
        System.out.println(result);

        return result;
    }
}
