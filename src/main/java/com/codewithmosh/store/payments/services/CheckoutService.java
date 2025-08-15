package com.codewithmosh.store.payments.services;

import com.codewithmosh.store.auth.config.AuthService;
import com.codewithmosh.store.cart.exceptions.CartEmptyException;
import com.codewithmosh.store.cart.exceptions.CartNotFoundException;
import com.codewithmosh.store.cart.repositories.CartRepository;
import com.codewithmosh.store.cart.services.CartService;
import com.codewithmosh.store.orders.entities.Order;
import com.codewithmosh.store.orders.repositories.OrderRepository;
import com.codewithmosh.store.payments.dtos.CheckoutRequestDto;
import com.codewithmosh.store.payments.dtos.CheckoutResponseDto;
import com.codewithmosh.store.payments.exceptions.PaymentException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CheckoutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;
    private final PaymentGateway paymentGateway;

    @Transactional
    public CheckoutResponseDto checkout(CheckoutRequestDto request) {
        var cart = cartRepository.getCartWithItems(request.getCartId())
            .orElse(null);

        if (cart == null) {
            throw new CartNotFoundException();
        }

        if (cart.isEmpty()) {
            throw new CartEmptyException();
        }

        var user = authService.getCurrentuser();
        var order = Order.fromCart(cart, user);
        orderRepository.save(order);

        try {
            var session = paymentGateway.createCheckoutSession(order);
            cartService.clearCart(cart.getCartId());
            return new CheckoutResponseDto(order.getId(), session.getCheckoutUrl());
        } catch (PaymentException e) {
            orderRepository.delete(order);
            throw e;
        }
    }

    public void handleWebhookEvent(WebhookRequest request) {
        paymentGateway
            .parseWebhookRequest(request)
            .ifPresent(paymentResult -> {
                var order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow();
                order.setStatus(paymentResult.getPaymentStatus());
                orderRepository.save(order);
            });
    }
}
