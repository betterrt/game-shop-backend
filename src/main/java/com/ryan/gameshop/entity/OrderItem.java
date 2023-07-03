package com.ryan.gameshop.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "Order_Item")
@Data
public class OrderItem {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", unique = true, nullable = false)
   private Integer id;

   @Column(name="game_id")
   private Integer gameId;

   @Column(name="price")
   private Double price;

   @Column(name="quantity")
   private Integer quantity;

   @ManyToOne
   @JoinColumn(name = "order_id")
   @JsonBackReference // * avoid infinite loop
   private Order order;
}
