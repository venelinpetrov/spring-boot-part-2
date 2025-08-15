package com.codewithmosh.store.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codewithmosh.store.user.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Boolean existsByEmail(String email);
    public Optional<User> findByEmail(String email);
}
