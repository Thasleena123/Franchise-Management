package com.example.franchisemanagement.sevice;

import com.example.franchisemanagement.Repository.SessionRepository;
import com.example.franchisemanagement.Repository.UserRepository;
import com.example.franchisemanagement.entity.SessionEntity;
import com.example.franchisemanagement.entity.UserEntity;
import com.example.franchisemanagement.enums.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private  final SessionRepository sessionRepository;
    private final Authenticate authenticate;
    @Autowired
    public UserService(UserRepository userRepository, SessionRepository sessionRepository, Authenticate authenticate) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.authenticate = authenticate;
    }
//    public ResponseEntity<String> registerUser(UserEntity user, String adminName) {
//        Optional<UserEntity> adminUser = Optional.ofNullable(userRepository.findByName(adminName));
//
//        if (adminUser.isPresent() && adminUser.get().getRole() == Role.ADMIN) {
//            user.setPassword(authenticate.encodePassword(user.getPassword()));
//            userRepository.save(user);
//            return ResponseEntity.ok("User registered successfully");
//        }
//        return ResponseEntity.status(403).body("Only ADMIN can register new users");
//    }

    public ResponseEntity<Map<String, String>> loginUser(String name, String password, HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> result = new HashMap<>();

        try {
            // Check if the user exists in the database
            Optional<UserEntity> user = Optional.ofNullable(userRepository.findByName(name));

            if (user.isPresent()) {

                // Check if the password is correct
                if (authenticate.checkPassword(password, user.get().getPassword())) {

                    UserEntity userEntity = user.get();

                    HttpSession session = request.getSession(false);
                    if (session != null) {
                        session.invalidate();
                    }


                    session = request.getSession(true);
                    session.setMaxInactiveInterval(86400);

                    Optional<SessionEntity> existingSession = Optional.ofNullable(sessionRepository.findByUserId(userEntity.getId()));

                    SessionEntity sessionEntity;
                    if (existingSession.isPresent()) {

                        sessionEntity = existingSession.get();
                        sessionEntity.setSessionId(session.getId());
                        sessionEntity.setCreatedAt(LocalDateTime.now());
                    } else {

                        sessionEntity = new SessionEntity();
                        sessionEntity.setSessionId(session.getId());
                        sessionEntity.setUserId(userEntity.getId());
                        sessionEntity.setCreatedAt(LocalDateTime.now());
                    }

                    sessionRepository.save(sessionEntity);

                    result.put("message", "Login Successful");
                    result.put("session_id", session.getId());
                    return ResponseEntity.ok(result);
                } else {
                    System.out.println("Incorrect password!");  // Log if password is incorrect
                    result.put("message", "Login Unsuccessful: Invalid credentials.");
                    return ResponseEntity.status(401).body(result);  // Unauthorized error
                }
            } else {
                System.out.println("User not found!");  // Log if user does not exist
                result.put("message", "Login Unsuccessful: User not found.");
                return ResponseEntity.status(401).body(result);  // Unauthorized error
            }
        } catch (Exception e) {
            // Log the error for debugging
            System.out.println("Error occurred during login: " + e.getMessage());
            e.printStackTrace();  // Print the stack trace to logs for further inspection
            result.put("message", "Login failed due to an internal error.");
            return ResponseEntity.status(500).body(result);  // Internal server error
        }
    }

}




