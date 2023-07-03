package com.ryan.gameshop.controller;

import com.ryan.gameshop.entity.CartItem;
import com.ryan.gameshop.entity.Game;
import com.ryan.gameshop.entity.Order;
import com.ryan.gameshop.entity.OrderItem;
import com.ryan.gameshop.entity.response.CartResponse;
import com.ryan.gameshop.entity.response.OrderResponse;
import com.ryan.gameshop.entity.response.PageResponse;
import com.ryan.gameshop.exception.GameNotFoundException;
import com.ryan.gameshop.exception.UserEmailMissingException;
import com.ryan.gameshop.service.GameService;
import com.ryan.gameshop.service.OrderService;
import com.ryan.gameshop.util.ExtractJWT;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/games/secure/order")
@CrossOrigin("http://localhost:3000")
public class OrderController {
   private OrderService orderService;
   private GameService gameService;

   @Autowired
   public OrderController(OrderService orderService, GameService gameService) {
      this.orderService = orderService;
      this.gameService = gameService;
   }

   @GetMapping("/all/{page}/{size}")
   public ResponseEntity<PageResponse> getUserOrders(
         @RequestHeader(value = "Authorization") String token,
         @PathVariable int page,
         @PathVariable int size) {

      String userEmail = ExtractJWT.extractUserEmail(token);
      Pageable pageable = PageRequest.of(page, size);

      Page<Order> thePage = orderService.getAllUserOrders(userEmail, pageable);

      return ResponseEntity.ok(PageResponse.builder()
            .totalPages(thePage.getTotalPages())
            .totalElements(thePage.getTotalElements())
            .dataList(thePage.getContent())
            .build());
   }

   @GetMapping("/detail")
   public ResponseEntity<List<OrderResponse>> getAllItemsInOrder(
         @RequestHeader(value = "Authorization") String token,
         @RequestParam int orderId)
         throws GameNotFoundException {

      List<OrderItem> itemList = orderService.getAllItemsInOrder(orderId);
      List<OrderResponse> responseList = new ArrayList<>();

      for(OrderItem orderItem: itemList) {
         Optional<Game> gameOptional = gameService.findGameById(orderItem.getGameId());
         if(!gameOptional.isPresent()) {
            throw new GameNotFoundException("Game Not Found!");
         }
         responseList.add(OrderResponse.builder()
               .orderItemId(orderItem.getId())
               .game(gameOptional.get())
               .quantity(orderItem.getQuantity())
               .purchasedPrice(orderItem.getPrice())
               .build());
      }
      return ResponseEntity.ok(responseList);
   }

   @PostMapping("/complete")
   public ResponseEntity<String> completeOrder() {
      return ResponseEntity.ok("Order Completed");
   }

   @PostMapping("/cancel")
   public ResponseEntity<String> cancelOrder() {
      return ResponseEntity.ok("Order cancelled");
   }

}
