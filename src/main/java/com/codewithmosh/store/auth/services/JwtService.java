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

    public Jwt generateToken(User user, long tokenExpiration) {
        var claims = Jwts.claims()
            .subject(user.getId().toString())
            .add("email", user.getEmail())
            .add("name", user.getName())
            .add("role", user.getRole().toString())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + tokenExpiration * 1000))
            .build();

        return new Jwt(claims, jwtConfig.getSecret());
    }

    public Jwt parseToken(String token) {
        try {
            var claims = Jwts.parser()
                .verifyWith(jwtConfig.getSecret())
                .build()
                .parseSignedClaims(token)
                .getPayload();
            return new Jwt(claims, jwtConfig.getSecret());
        } catch (JwtException e) {
            return null;
        }
    }
}
