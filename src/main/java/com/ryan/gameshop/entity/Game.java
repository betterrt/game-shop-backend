package com.ryan.gameshop.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "Game")
@Data
public class Game {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", unique = true, nullable = false)
   private Integer id;

   @Column(name = "name")
   private String name;

   @Column(name = "released_date")
   private Date releasedDate;

   @Column(name = "developer")
   private String developer;

   @Column(name = "publisher")
   private String publisher;

   @Column(name = "description")
   private String description;

   @Column(name = "nums")
   private Integer nums;

   @Column(name = "price")
   private Double price;

   @Column(name = "image")
   private String image;


}
