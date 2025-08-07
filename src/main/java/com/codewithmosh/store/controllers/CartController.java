package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.AddItemToCartDto;
import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.dtos.CartItemDto;
import com.codewithmosh.store.dtos.UpdateCartItemDto;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/carts")
@AllArgsConstructor
public class CartController {
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;
    private CartRepository cartRepository;

    @PostMapping
    public ResponseEntity<CartDto> createCart(UriComponentsBuilder uriBuilder) {
        var cart = new Cart();

        cartRepository.save(cart);

        var cartDto = cartMapper.toDto(cart);
        var uri = uriBuilder.path("/carts/{cart_id}").buildAndExpand(cartDto.getCartId()).toUri();

        return ResponseEntity.created(uri).body(cartDto);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addToCart(
        @PathVariable(name = "cartId") UUID cartId,
        @RequestBody AddItemToCartDto body) {

        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }

        var product = productRepository.findById(body.getProductId()).orElse(null);

        if (product == null) {
            return ResponseEntity.badRequest().build();
        }

        var cartItem = cart.addItem(product);

        cartRepository.save(cart);

        var cartItemDto = cartMapper.toDto(cartItem);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable(name = "cartId")  UUID cartId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);

        if (cart == null) {
            return ResponseEntity.notFound().build();
        }

        var cartDto = cartMapper.toDto(cart);

        return ResponseEntity.ok(cartDto);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateCartItem(
        @PathVariable(name = "cartId") UUID cartId,
        @PathVariable(name = "productId") Byte productId,
        @Valid @RequestBody UpdateCartItemDto body) {

        var cart = cartRepository.getCartWithItems(cartId).orElse(null);

        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error", "Cart with id " + cartId + " not found.")
            );
        }

        var cartItem = cart.getItem(productId);

        if (cartItem == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error", "Product with id " + productId + " not found.")
            );
        }

        cartItem.setQuantity(body.getQuantity());
        cartRepository.save(cart);
        var cartItemDto = cartMapper.toDto(cartItem);

        return ResponseEntity.ok(cartItemDto);
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> deleteCartItem(
        @PathVariable("cartId") UUID cartId,
        @PathVariable("productId") Byte productId) {

        var cart = cartRepository.getCartWithItems(cartId).orElse(null);

        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error", "Cart was not found")
            );
        }

        cart.removeItem(productId);
        cartRepository.save(cart);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<Void> clearCart(@PathVariable("cartId") UUID cartId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }

        cart.clear();

        cartRepository.save(cart);

        return ResponseEntity.noContent().build();

    }
}
