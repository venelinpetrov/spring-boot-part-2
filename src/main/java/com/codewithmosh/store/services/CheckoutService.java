package com.codewithmosh.store.services;

import com.codewithmosh.store.config.AuthService;
import com.codewithmosh.store.dtos.CheckoutRequestDto;
import com.codewithmosh.store.dtos.CheckoutResponseDto;
import com.codewithmosh.store.entities.Order;
import com.codewithmosh.store.exceptions.CartEmptyException;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CheckoutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;

    public CheckoutResponseDto checkout(CheckoutRequestDto request) {
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);

        if (cart == null) {
            throw new CartNotFoundException();
        }


        if (cart.isEmpty()) {
            throw new CartEmptyException();
        }

        var user = authService.getCurrentuser();
        var order = Order.fromCart(cart, user);
        orderRepository.save(order);
        cartService.clearCart(cart.getCartId());

        return new CheckoutResponseDto(order.getId());
    }
}
