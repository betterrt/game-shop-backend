package com.ryan.gameshop.controller;

import com.ryan.gameshop.entity.CartItem;
import com.ryan.gameshop.entity.Game;
import com.ryan.gameshop.entity.response.CartResponse;
import com.ryan.gameshop.exception.GameNotFoundException;
import com.ryan.gameshop.exception.UserEmailMissingException;
import com.ryan.gameshop.service.CartService;
import com.ryan.gameshop.service.GameService;
import com.ryan.gameshop.service.OrderService;
import com.ryan.gameshop.util.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/games/secure/cart")
@CrossOrigin("http://localhost:3000")
public class CartController {
   private CartService cartService;
   private GameService gameService;
   private OrderService orderService;

   @Autowired
   public CartController(CartService cartService, GameService gameService,
                         OrderService orderService) {
      this.cartService = cartService;
      this.gameService = gameService;
      this.orderService = orderService;
   }

   @PostMapping("/add")
   public ResponseEntity<String> addGameToCart(
         @RequestHeader(value = "Authorization") String token,
         @RequestParam int gameId) throws UserEmailMissingException{

      String userEmail = ExtractJWT.extractUserEmail(token);
      cartService.addGameToCart(userEmail, gameId);
      return ResponseEntity.ok("Game successfully added to the cart");
   }

   @PostMapping("/subtract")
   public ResponseEntity<String> subtractGameFromCart(
         @RequestHeader(value = "Authorization") String token,
         @RequestParam int cartItemId) {

      cartService.subtractGameFromCart(cartItemId);
      return ResponseEntity.ok("Game quantity subtracted by 1");
   }

   @PostMapping("/remove")
   public ResponseEntity<String> removeGameFromCart(
         @RequestHeader(value = "Authorization") String token,
         @RequestParam int cartItemId) {

      cartService.removeGameFromCart(cartItemId);
      return ResponseEntity.ok("Game successfully removed.");
   }

   @GetMapping("/all")
   public ResponseEntity<List<CartResponse>> getAllItemsInCart(
         @RequestHeader(value = "Authorization") String token)
         throws UserEmailMissingException, GameNotFoundException{

      String userEmail = ExtractJWT.extractUserEmail(token);
      List<CartItem> itemList = cartService.getAllItemsInCart(userEmail);

      List<CartResponse> responseList = new ArrayList<>();
      for(CartItem cartItem: itemList) {
         Optional<Game> gameOptional = gameService.findGameById(cartItem.getGameId());
         if(!gameOptional.isPresent()) {
            throw new GameNotFoundException("Game Not Found!");
         }
         responseList.add(CartResponse.builder()
               .cartItemId(cartItem.getId())
               .game(gameOptional.get())
               .quantity(cartItem.getQuantity())
               .build());
      }
      return ResponseEntity.ok(responseList);
   }

   @PostMapping("/quantity")
   public ResponseEntity<Integer> findGameQuantityInCart(
         @RequestHeader(value = "Authorization") String token,
         @RequestParam int gameId) throws UserEmailMissingException {

      String userEmail = ExtractJWT.extractUserEmail(token);
      return ResponseEntity.ok(cartService.findGameQuantityInCart(userEmail, gameId));
   }

   @PostMapping("/checkout")
   public ResponseEntity<Boolean> checkoutCart(
         @RequestHeader(value = "Authorization") String token)
         throws UserEmailMissingException, GameNotFoundException {

      String userEmail = ExtractJWT.extractUserEmail(token);
      // place order
      Boolean isSuccess = orderService.placeOrder(userEmail, cartService.getAllItemsInCart(userEmail));

      if(isSuccess) {
         // delete all related data of the cart
         cartService.deleteCart(userEmail);
      }
      return ResponseEntity.ok(isSuccess);
   }

}
