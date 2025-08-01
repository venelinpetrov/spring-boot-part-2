package com.codewithmosh.store.repositories;

import com.codewithmosh.store.entities.Product;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = "category")
    List<Product> findByCategoryId(Integer categoryId);

    @Query("SELECT p FROM Product p")
    @EntityGraph(attributePaths = "category")
    List<Product> findAllWithCategory();
}