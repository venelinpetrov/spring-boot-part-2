package com.codewithmosh.store.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    @Value("${spring.jwt.secret}")
    private String secret;
    public String generateToken(String email) {
        final long tokenExpiration = 24 * 60 * 60 * 1000;
        return Jwts.builder()
            .subject(email)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + tokenExpiration))
            .signWith(Keys.hmacShaKeyFor((secret.getBytes()))) // todo remove secret from code and store it securely
            .compact();
    }
}
