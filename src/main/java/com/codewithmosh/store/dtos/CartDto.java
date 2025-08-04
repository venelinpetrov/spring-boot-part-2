package com.codewithmosh.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class CartDto {
    private String id;
    private Set<CartItemDto> items;
    BigDecimal totalPrice;
}
