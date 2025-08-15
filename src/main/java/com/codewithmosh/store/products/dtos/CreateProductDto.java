package com.codewithmosh.store.products.dtos;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CreateProductDto {
    private String name;
    private String description;
    private BigDecimal price;
    private Byte categoryId;
}
