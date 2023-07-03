package com.ryan.gameshop.controller;

import com.ryan.gameshop.entity.Order;
import com.ryan.gameshop.entity.request.AddGameRequest;
import com.ryan.gameshop.entity.response.PageResponse;
import com.ryan.gameshop.exception.CategoryNotFoundException;
import com.ryan.gameshop.exception.GameNotFoundException;
import com.ryan.gameshop.exception.NotAdminException;
import com.ryan.gameshop.exception.OrderNotFoundException;
import com.ryan.gameshop.service.AdminService;
import com.ryan.gameshop.service.GameService;
import com.ryan.gameshop.service.OrderService;
import com.ryan.gameshop.util.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/secure")
@CrossOrigin("http://localhost:3000")
public class AdminController {
   private AdminService adminService;
   private OrderService orderService;
   private GameService gameService;

   @Autowired
   public AdminController(AdminService adminService, OrderService orderService,
                          GameService gameService) {
      this.adminService = adminService;
      this.orderService = orderService;
      this.gameService = gameService;
   }

   @PostMapping("/add/game")
   public void addGame(@RequestHeader(value = "Authorization") String token,
                       @RequestBody AddGameRequest addGameRequest) throws
         NotAdminException, CategoryNotFoundException {

      if (!ExtractJWT.isAdmin(token)) {
         throw new NotAdminException("You Have No Access");
      }
      adminService.addNewGame(addGameRequest);
   }

   @PutMapping("/quantity/increase")
   public void increaseQuantity(
         @RequestHeader(value = "Authorization") String token,
         @RequestParam int gameId)
         throws GameNotFoundException, NotAdminException {

      if (!ExtractJWT.isAdmin(token)) {
         throw new NotAdminException("You Have No Access");
      }
      gameService.increaseQuantity(gameId, 1);
   }

   @PutMapping("/quantity/decrease")
   public void decreaseQuantity(
         @RequestHeader(value = "Authorization") String token,
         @RequestParam int gameId)
         throws GameNotFoundException, NotAdminException {

      if (!ExtractJWT.isAdmin(token)) {
         throw new NotAdminException("You Have No Access");
      }
      gameService.decreaseQuantity(gameId, 1);
   }

   @PutMapping("/price")
   public void updatePrice(
         @RequestHeader(value = "Authorization") String token,
         @RequestParam int gameId,
         @RequestParam double price)
         throws GameNotFoundException, NotAdminException {

      if (!ExtractJWT.isAdmin(token)) {
         throw new NotAdminException("You Have No Access");
      }
      gameService.updatePrice(gameId, price);
   }

   @PostMapping("/order/complete")
   public void completeOrder(
         @RequestHeader(value = "Authorization") String token,
         @RequestParam int orderId)
         throws NotAdminException, OrderNotFoundException {

      if (!ExtractJWT.isAdmin(token)) {
         throw new NotAdminException("You Have No Access");
      }
      orderService.completeOrder(orderId);

   }

   @PostMapping("/order/cancel")
   public void cancelOrder(
         @RequestHeader(value = "Authorization") String token,
         @RequestParam int orderId)
         throws NotAdminException, OrderNotFoundException {

      if (!ExtractJWT.isAdmin(token)) {
         throw new NotAdminException("You Have No Access");
      }
      orderService.cancelOrder(orderId);
   }

   @GetMapping("/order/all/{page}/{size}")
   public ResponseEntity<PageResponse> getAllOrders(
         @RequestHeader(value = "Authorization") String token,
         @PathVariable int page,
         @PathVariable int size) throws NotAdminException{

      if (!ExtractJWT.isAdmin(token)) {
         throw new NotAdminException("You Have No Access");
      }
      Pageable pageable = PageRequest.of(page, size);
      Page<Order> thePage = orderService.getAllOrders(pageable);

      return ResponseEntity.ok(PageResponse.builder()
            .totalPages(thePage.getTotalPages())
            .totalElements(thePage.getTotalElements())
            .dataList(thePage.getContent())
            .build());
   }
}
