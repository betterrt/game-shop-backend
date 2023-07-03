package com.ryan.gameshop.entity.request;

import lombok.Data;
import java.util.Optional;

@Data
public class ReviewRequest {
   private double rating;
   private int gameId;
   private Optional<String> reviewDescription;
}
