package com.codewithmosh.store.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProductDto {
    Integer id;
    String name;
    String description;
    BigDecimal price;
    Byte categoryId;
}
