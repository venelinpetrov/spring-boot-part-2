package com.codewithmosh.store.cart.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddItemToCartDto {
    @NotNull
    private Long productId;
}
