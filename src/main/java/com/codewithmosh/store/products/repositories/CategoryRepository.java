package com.codewithmosh.store.products.repositories;

import org.springframework.data.repository.CrudRepository;

import com.codewithmosh.store.products.entities.Category;

public interface CategoryRepository extends CrudRepository<Category, Byte> {
}