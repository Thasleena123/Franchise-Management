package com.example.franchisemanagement.sevice;

import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Component
public class Authenticate {

    public String encodePassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password,hashedPassword);
    }
}
