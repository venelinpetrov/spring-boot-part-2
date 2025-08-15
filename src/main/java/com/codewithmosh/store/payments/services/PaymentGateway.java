package com.codewithmosh.store.payments.services;

import java.util.Optional;

import com.codewithmosh.store.orders.entities.Order;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession(Order order);
    Optional<PaymentResult> parseWebhookRequest(WebhookRequest request);
}
