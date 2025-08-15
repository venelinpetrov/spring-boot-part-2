package com.codewithmosh.store.products.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import com.codewithmosh.store.products.dtos.CreateProductDto;
import com.codewithmosh.store.products.dtos.ProductDto;
import com.codewithmosh.store.products.dtos.UpdateProductDto;
import com.codewithmosh.store.products.entities.Product;
import com.codewithmosh.store.products.mappers.ProductMapper;
import com.codewithmosh.store.products.repositories.CategoryRepository;
import com.codewithmosh.store.products.repositories.ProductRepository;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;


@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @GetMapping
    public Iterable<ProductDto> getProducts(@RequestParam(required = false, defaultValue = "", name="categoryId") Integer categoryId) {
        List<Product> products;
        if (categoryId == null) {
            products = productRepository.findAllWithCategory();
        } else {
            products = productRepository.findByCategoryId(categoryId);
        }
        return products.stream()
            .map(productMapper::toDto)
            .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long id) {
        var product = productRepository.findById(id).orElse(null);

        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(productMapper.toDto(product));
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody CreateProductDto data, UriComponentsBuilder uriBuilder) {
        var category = categoryRepository.findById(data.getCategoryId()).orElse(null);

        if (category == null) {
            return ResponseEntity.badRequest().build();
        }

        var productEntity = productMapper.toEntity(data);

        productEntity.setCategory(category);

        productRepository.save(productEntity);

        var productDto = productMapper.toDto(productEntity);

        var uri = uriBuilder
            .path("/products/{id}")
            .buildAndExpand(productDto.getId())
            .toUri();

        return ResponseEntity.created(uri).body(productDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable(name = "id") Long id, @RequestBody UpdateProductDto data) {
        var category = categoryRepository.findById(data.getCategoryId()).orElse(null);

        if (category == null) {
            return ResponseEntity.badRequest().build();
        }

        var productEntity = productRepository.findById(id).orElse(null);

        if (productEntity == null) {
            return ResponseEntity.notFound().build();
        }

        productMapper.update(data, productEntity);
        productEntity.setCategory(category);
        productRepository.save(productEntity);

        var productDto = productMapper.toDto(productEntity);

        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        var productEntity = productRepository.findById(id).orElse(null);

        if (productEntity == null) {
            return ResponseEntity.notFound().build();
        }

        productRepository.delete(productEntity);

        return ResponseEntity.noContent().build();
    }
}
