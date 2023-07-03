package com.ryan.gameshop.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Cart")
@Data
public class Cart {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", unique = true, nullable = false)
   private Integer id;

   @Column(name="user_email")
   private String userEmail;

   //CascadeType.ALL: if you delete an order, all its related items will be deleted
   @OneToMany(fetch = FetchType.LAZY, mappedBy = "cart", cascade = CascadeType.ALL)
   @ToString.Exclude //
   @JsonManagedReference // *to avoid infinite loop
   private List<CartItem> cartItemList = new ArrayList<>(0);

   public void addCartItem(CartItem cartItem) {
      this.cartItemList.add(cartItem);
   }

}
