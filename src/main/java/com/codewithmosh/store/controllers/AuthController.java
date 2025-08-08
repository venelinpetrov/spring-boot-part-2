package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.LoginDto;
import com.codewithmosh.store.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginDto body) {
        var user = userRepository.findByEmail(body.getEmail()).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!passwordEncoder.matches(body.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok().build();
    }
}