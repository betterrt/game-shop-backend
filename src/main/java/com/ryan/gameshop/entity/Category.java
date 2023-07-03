package com.ryan.gameshop.entity;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "Category")
@Data
public class Category {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", unique = true, nullable = false)
   private Integer id;

   @Column(name = "name")
   private String name;

}
