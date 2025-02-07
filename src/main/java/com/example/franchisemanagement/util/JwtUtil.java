package com.example.franchisemanagement.util;
import java.security.Key;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import io.jsonwebtoken.*;

import org.springframework.stereotype.Component;

        @Component
        public class JwtUtil {
            private static final String SECRET_KEY = "123444567788hhfhjjhhfjhghvggjhhjfhjfhgjhhhjhhgxyyt"; // Must be at least 32 chars
            private static final long EXPIRATION_TIME = 86400000; // 1 day (in milliseconds)

            private Key getSigningKey() {
                byte[] keyBytes = SECRET_KEY.getBytes();  // Convert string to byte array
                return new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256");
            }

            public String generateToken(String username) {
                return Jwts.builder()
                        .setSubject(username)
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                        .compact();
            }

            public String extractUsername(String token) {
                try {
                    JwtParser parser = Jwts.parserBuilder().setSigningKey(getSigningKey()).build();
                    return parser.parseClaimsJws(token).getBody().getSubject();
                } catch (JwtException e) {
                    return null; // Invalid token
                }
            }

            public boolean validateToken(String token) {
                try {
                    JwtParser parser = Jwts.parserBuilder().setSigningKey(getSigningKey()).build();
                    parser.parseClaimsJws(token);
                    return true;
                } catch (JwtException e) {
                    return false; // Invalid token
                }
            }
        }
