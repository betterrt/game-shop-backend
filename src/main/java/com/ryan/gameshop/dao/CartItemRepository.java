package com.ryan.gameshop.dao;

import com.ryan.gameshop.entity.CartItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
   Optional<CartItem> findByCartIdAndGameId(int cartId, int gameId);
   List<CartItem> findByCartId(int cartId);
}
