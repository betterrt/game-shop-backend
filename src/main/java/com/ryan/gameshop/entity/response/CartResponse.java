package com.ryan.gameshop.entity.response;

import com.ryan.gameshop.entity.Game;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@Data
@ToString
public class CartResponse {
   private int cartItemId;
   private Game game;
   private int quantity;
}
