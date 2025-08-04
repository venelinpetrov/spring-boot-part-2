package com.codewithmosh.store.repositories;


import com.codewithmosh.store.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, String> {

    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.items i LEFT JOIN FETCH i.product WHERE c.cartId = :id")
    Optional<Cart> findWithItemsAndProductsById(@Param("id") String id);
}
