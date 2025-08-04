package com.codewithmosh.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class CartItemDto {
    private ProductDto product;
    private int quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;
}
