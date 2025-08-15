package com.codewithmosh.store.products.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.codewithmosh.store.products.dtos.CreateProductDto;
import com.codewithmosh.store.products.dtos.ProductDto;
import com.codewithmosh.store.products.dtos.UpdateProductDto;
import com.codewithmosh.store.products.entities.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    public ProductDto toDto(Product product);
    public Product toEntity(CreateProductDto product);
    void update(UpdateProductDto productDto, @MappingTarget Product productEntity);
}
