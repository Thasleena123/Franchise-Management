package com.example.franchisemanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor()
@AllArgsConstructor
@Data
@Entity
public class SessionEntity {

    @Column(name = "session_id")
    private   String sessionId;
    @Id
    @JoinColumn(name = "user_id", nullable = false)
    private   int userId;

    private LocalDateTime createdAt;
}
