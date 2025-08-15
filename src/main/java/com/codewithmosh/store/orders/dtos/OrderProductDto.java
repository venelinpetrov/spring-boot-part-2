package com.codewithmosh.store.orders.dtos;

import lombok.Data;

import java.math.BigDecimal;

import com.codewithmosh.store.cart.dtos.CartProductDto;

@Data
public class OrderProductDto {
    private CartProductDto product;
    private int quantity;
    private BigDecimal totalPrice;
}
