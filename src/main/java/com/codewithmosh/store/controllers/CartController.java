package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.*;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.ProductNotFoundException;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.services.CartService;
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
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartDto> createCart(UriComponentsBuilder uriBuilder) {
        var cartDto = cartService.createCart();
        var uri = uriBuilder.path("/carts/{cart_id}").buildAndExpand(cartDto.getCartId()).toUri();

        return ResponseEntity.created(uri).body(cartDto);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addToCart(
        @PathVariable(name = "cartId") UUID cartId,
        @RequestBody AddItemToCartDto body) {

        var cartItemDto = cartService.addToCart(cartId, body.getProductId());

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable(name = "cartId")  UUID cartId) {
        var cartDto = cartService.getCart(cartId);
        return ResponseEntity.ok(cartDto);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<CartItemDto> updateCartItem(
        @PathVariable(name = "cartId") UUID cartId,
        @PathVariable(name = "productId") Byte productId,
        @Valid @RequestBody UpdateCartItemDto body) {
        var cartItemDto = cartService.updateCartItem(cartId, productId, body.getQuantity());

        return ResponseEntity.ok(cartItemDto);
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<Void> deleteCartItem(
        @PathVariable("cartId") UUID cartId,
        @PathVariable("productId") Byte productId) {
        cartService.deleteCartItem(cartId, productId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<Void> clearCart(@PathVariable("cartId") UUID cartId) {
        cartService.clearCart(cartId);

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorDto> handleCartNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            new ErrorDto("Cart not found")
        ) ;
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDto> handleProductNotFound() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            new ErrorDto("Product not found in cart")
        );
    }
}
