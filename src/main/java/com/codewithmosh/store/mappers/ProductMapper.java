package com.codewithmosh.store.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.codewithmosh.store.dtos.CreateProductDto;
import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.dtos.UpdateProductDto;
import com.codewithmosh.store.entities.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "categoryName", source = "product.category.name")
    public ProductDto toDto(Product product);

    public Product toEntity(CreateProductDto product);

    void update(UpdateProductDto productDto, @MappingTarget Product productEntity);
}
