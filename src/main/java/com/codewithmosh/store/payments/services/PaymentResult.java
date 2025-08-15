package com.codewithmosh.store.payments.services;

import com.codewithmosh.store.payments.entities.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PaymentResult {
    private Long orderId;
    private PaymentStatus paymentStatus;
}
