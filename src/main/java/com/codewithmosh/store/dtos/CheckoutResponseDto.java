package com.codewithmosh.store.dtos;

import lombok.Data;

@Data
public class CheckoutResponseDto {
    private Long orderId;
    private String checkoutUrl;

    public CheckoutResponseDto(Long orderId, String url) {
        this.orderId = orderId;
        this.checkoutUrl = url;
    }
}
