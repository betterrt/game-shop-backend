package com.ryan.gameshop.dao;

import com.ryan.gameshop.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
   Page<Order> findByUserEmailOrderByIdDesc(String userEmail, Pageable pageable);

   Page<Order> findAll(Pageable pageable);

}

