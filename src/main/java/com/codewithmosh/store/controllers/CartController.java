package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.dtos.CartItemDto;
import com.codewithmosh.store.mappers.ProductMapper;
import com.codewithmosh.store.repositories.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/carts")
@AllArgsConstructor
public class CartController {
    private CartRepository cartRepository;
    private ProductMapper productMapper;

    @GetMapping("/{id}")
    public ResponseEntity<CartDto> getCart(@PathVariable(name = "id") String id) {
        var cartEntity =  cartRepository.findWithItemsAndProductsById(id).orElse(null);
        if (cartEntity == null) {
            return ResponseEntity.notFound().build();
        }

        BigDecimal totalPrice = cartEntity.getItems()
                .stream()
                .map(i -> i.getProduct().getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Set<CartItemDto> cartItems = cartEntity.getItems().stream()
            .map(i -> new CartItemDto(
                productMapper.toDto(i.getProduct()),
                i.getQuantity(),
                i.getProduct().getPrice(),
                BigDecimal.ZERO))
            .collect(Collectors.toSet());


        var cartDto = new CartDto(cartEntity.getCartId(), cartItems, totalPrice);

        for (var item : cartDto.getItems()) {
            if (item.getPrice() != null) {
                BigDecimal subtotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                item.setTotalPrice(subtotal);
            } else {
                item.setTotalPrice(BigDecimal.ZERO);
            }
        }

        BigDecimal total = cartDto.getItems()
            .stream()
            .map(CartItemDto::getTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        cartDto.setTotalPrice(total);

        return ResponseEntity.ok(cartDto);
    }
}
