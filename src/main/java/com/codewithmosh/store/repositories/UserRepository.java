package com.codewithmosh.store.repositories;

import com.codewithmosh.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Boolean existsByEmail(String email);
    public Optional<User> findByEmail(String email);
}
