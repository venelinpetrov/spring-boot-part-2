package com.codewithmosh.store.cart.repositories;


import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codewithmosh.store.cart.entities.Cart;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    @EntityGraph(attributePaths = "items.product")
    @Query("SELECT c FROM Cart c WHERE c.cartId = :cartId")
    Optional<Cart> getCartWithItems(@Param("cartId") UUID cartId);
}
