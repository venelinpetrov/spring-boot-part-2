package com.codewithmosh.store.auth.services;

import com.codewithmosh.store.auth.config.JwtConfig;
import com.codewithmosh.store.user.entities.User;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Date;

@AllArgsConstructor
@Service
public class JwtService {
    private final JwtConfig jwtConfig;

    public Jwt generateAccessToken(User user) {
        return generateToken(user, jwtConfig.getAccessTokenExpiration());
    }

    public Jwt generateRefreshToken(User user) {
        return generateToken(user, jwtConfig.getRefreshTokenExpiration());
    }

    private Jwt generateToken(User user, long tokenExpirationSeconds) {
        Date now = new Date();
        Date expiryDate = new Date(System.currentTimeMillis() + tokenExpirationSeconds * 1000);

        var claims = Jwts.claims()
            .subject(user.getId().toString())
            .add("email", user.getEmail())
            .add("name", user.getName())
            .add("role", user.getRole().toString())
            .issuedAt(now)
            .expiration(expiryDate)
            .build();

        String token = Jwts.builder()
            .claims(claims)
            .signWith(jwtConfig.getSigningKey())
            .compact();

        return new Jwt(claims, token);
    }

    public Jwt parseToken(String token) {
        try {
            var claims = Jwts.parser()
                .verifyWith(jwtConfig.getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

            return new Jwt(claims, token);
        } catch (JwtException e) {
            System.out.println("Invalid JWT: " + e.getMessage());
            return null;
        }
    }
}