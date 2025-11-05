package com.musicrecommender.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

    // ⚠️ Make sure this secret is at least 256-bit (32+ characters) Base64
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // ✅ Signing key generate karna
    private Key getSigningKey() {
        // Secret ko Base64 decode karo
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // ✅ Token generate karna
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ Token se username extract karna
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // ✅ Token validate karna
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
