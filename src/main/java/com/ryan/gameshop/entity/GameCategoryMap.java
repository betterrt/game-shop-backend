package com.ryan.gameshop.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "Game_Category")
@Data
public class GameCategoryMap {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", unique = true, nullable = false)
   private Integer id;

   @Column(name = "fk_game_id")
   private Integer gameId;

   @Column(name = "fk_category_id")
   private Integer categoryId;
}
