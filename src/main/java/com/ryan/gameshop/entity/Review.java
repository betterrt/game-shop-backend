package com.ryan.gameshop.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Review")
@Data
public class Review {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id")
   private int id;

   @Column(name = "game_id")
   private int gameId;

   @Column(name = "user_email")
   private String userEmail;

   @Column(name = "review_date")
   @CreationTimestamp
   private Date reviewDate;

   @Column(name = "rating")
   private double rating;

   @Column(name = "review_description")
   private String reviewDescription;


}
