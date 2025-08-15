package com.codewithmosh.store.cart.exceptions;

public class CartNotFoundException extends RuntimeException{
    public CartNotFoundException() {
        super("Cart not found");
    }
}
