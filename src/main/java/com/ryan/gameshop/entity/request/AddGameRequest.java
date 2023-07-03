package com.ryan.gameshop.entity.request;

import lombok.Data;

import java.util.*;

@Data
public class AddGameRequest {
   private String name;

   private String releasedDate;

   private String developer;

   private String publisher;

   private String description;

   private Integer nums;

   private Double price;

   private String image;

   private String[] categories;
}
