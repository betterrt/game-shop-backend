package com.ryan.gameshop.dao;


import com.ryan.gameshop.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
   List<OrderItem> findByOrderId(int orderId);

}
