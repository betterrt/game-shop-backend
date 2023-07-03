package com.ryan.gameshop.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "Cart_Item")
@Data
public class CartItem {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", unique = true, nullable = false)
   private Integer id;

   @Column(name="game_id")
   private Integer gameId;

   @Column(name="quantity")
   private Integer quantity;

   @ManyToOne
   @JoinColumn(name = "cart_id")
   @JsonBackReference // * avoid infinite loop
   private Cart cart;
}
