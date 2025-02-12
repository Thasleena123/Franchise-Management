package com.example.franchisemanagement.Repository;

import com.example.franchisemanagement.entity.FranchiseStockEntity;
import com.example.franchisemanagement.entity.SessionEntity;
import com.example.franchisemanagement.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface SessionRepository  extends JpaRepository<SessionEntity, String> {
    SessionEntity findBySessionId(String sessionId);
    SessionEntity findByUserId(int userId);
    void deleteByUserId(int user);


}
