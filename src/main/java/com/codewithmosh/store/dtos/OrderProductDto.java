package com.codewithmosh.store.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderProductDto {
    private CartProductDto product;
    private int quantity;
    private BigDecimal totalPrice;
}
