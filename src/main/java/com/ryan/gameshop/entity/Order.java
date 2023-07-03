package com.ryan.gameshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "Orders")
@Data
public class Order {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", unique = true, nullable = false)
   private Integer id;

   @Column(name = "refer_number")
   private Long referNumber;

   @Column(name="user_email")
   private String userEmail;

   @Column(name="order_status")
   private String orderStatus;

   @Column(name = "placed_date")
   @CreationTimestamp
   private Date placedDate;

   //CascadeType.ALL: if you delete an order, all its related items will be deleted
   @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL)
   @ToString.Exclude
   @JsonManagedReference // *to avoid infinite loop
   private List<OrderItem> orderItemList = new ArrayList<>(0);

   public void addOrderItem(OrderItem orderItem) {
      this.orderItemList.add(orderItem);
   }

}
