package com.codewithmosh.store.controllers;

import com.codewithmosh.store.config.AuthService;
import com.codewithmosh.store.dtos.CheckoutRequestDto;
import com.codewithmosh.store.dtos.CheckoutResponseDto;
import com.codewithmosh.store.dtos.ErrorDto;
import com.codewithmosh.store.entities.Order;
import com.codewithmosh.store.entities.OrderStatus;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.OrderRepository;
import com.codewithmosh.store.services.CartService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {
    private final AuthService authService;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final CartService cartService;

    @PostMapping
    @Transactional
    public ResponseEntity<?> checkout(@Valid @RequestBody CheckoutRequestDto request) {
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);

        if (cart == null) {
            return ResponseEntity.badRequest().body(new ErrorDto("Cart not found"));
        }

        var items = cart.getItems();

        if (items.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorDto("Cart is empty"));
        }

        var user = authService.getCurrentuser();

        var order = new Order();
        order.setCustomer(user);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalPrice(cart.getTotalPrice());
        order.addItems(cart.getItems());

        orderRepository.save(order);

        cartService.clearCart(cart.getCartId());

        return ResponseEntity.ok(new CheckoutResponseDto(order.getId()));
    }
}
