package com.codewithmosh.store.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProductDto {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
}
