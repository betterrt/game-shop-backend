package com.ryan.gameshop.dao;

import com.ryan.gameshop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
   Optional<Cart> findByUserEmail(String userEmail);
   void deleteByUserEmail(String userEmail);
}
