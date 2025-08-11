package com.codewithmosh.store.dtos;

import lombok.Data;

@Data
public class CheckoutResponseDto {
    private Long orderId;

    public CheckoutResponseDto(Long orderId) {
        this.orderId = orderId;
    }
}
