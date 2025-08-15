package com.codewithmosh.store.products.dtos;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class UpdateProductDto {
    private String name;
    private String description;
    private BigDecimal price;
    private Byte categoryId;
}
