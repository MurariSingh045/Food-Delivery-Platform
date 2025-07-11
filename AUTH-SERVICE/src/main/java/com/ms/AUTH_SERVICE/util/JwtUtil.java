package com.ms.AUTH_SERVICE.util;

import com.ms.AUTH_SERVICE.model.Roles;
import com.ms.AUTH_SERVICE.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    private static final String SECRET = "murari-secure-key-for-jwt-12345678murari";
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
    private static final long EXPIRATION_TIME_MS = 1000 * 60 * 60; // 1 hour

    // generating the token
    public String generateToken(User user) {
        List<String> roles = user.getRoles().stream()
                .map(Roles::getName)
                .toList();

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("roles", roles)
                .claim("userId", user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(SECRET_KEY)
                .compact();
    }

    // extract email from token
    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    // extract role from token
    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return parseClaims(token).get("roles", List.class);
    }

    // extract user id from the token
    public Long extractUserId(String token) {
        return parseClaims(token).get("userId", Long.class);
    }

    // validating the token
    public boolean validateToken(String token, String expectedEmail) {
        return extractEmail(token).equals(expectedEmail) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return parseClaims(token).getExpiration().before(new Date());
    }

    // extracting all claims here (claims include (sub(email) , role , id , exp)
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY) // using same secret key
                .build()
                .parseClaimsJws(token.trim()) // ✅ Trim whitespace
                .getBody();
    }
}
