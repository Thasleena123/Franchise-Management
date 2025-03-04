package com.example.franchisemanagement.Repository;

import com.example.franchisemanagement.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Integer> {
    UserEntity findByName(String name);
    UserEntity findUserById(int id);
    UserEntity findFranchiseIdById(int id);


}