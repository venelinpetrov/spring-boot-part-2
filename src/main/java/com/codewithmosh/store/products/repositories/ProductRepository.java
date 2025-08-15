package com.codewithmosh.store.products.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.codewithmosh.store.products.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = "category")
    List<Product> findByCategoryId(Integer categoryId);

    @Query("SELECT p FROM Product p")
    @EntityGraph(attributePaths = "category")
    List<Product> findAllWithCategory();
}