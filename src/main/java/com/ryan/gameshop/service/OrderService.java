package com.ryan.gameshop.service;

import com.ryan.gameshop.dao.OrderItemRepository;
import com.ryan.gameshop.dao.OrderRepository;
import com.ryan.gameshop.entity.*;
import com.ryan.gameshop.exception.GameNotFoundException;
import com.ryan.gameshop.exception.OrderNotFoundException;
import com.ryan.gameshop.util.OrderRefer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class OrderService {
   private OrderRepository orderRepository;
   private OrderItemRepository orderItemRepository;
   private GameService gameService;
   public static final String COMPLETED = "Completed";
   public static final String CANCELED = "Canceled";
   public static final String PROCESSING = "Processing";

   @Autowired
   public void setOrderRepository(OrderRepository orderRepository) {
      this.orderRepository = orderRepository;
   }

   @Autowired
   public void setOrderItemRepository(OrderItemRepository orderItemRepository) {
      this.orderItemRepository = orderItemRepository;
   }

   @Autowired
   public void setGameService(GameService gameService) {
      this.gameService = gameService;
   }
   /**
    * Get all the orders of the user, in descending order of placedDate.
    * @param userEmail
    * @param pageable
    * @return
    */
   public Page<Order> getAllUserOrders(String userEmail, Pageable pageable) {

      return orderRepository.findByUserEmailOrderByIdDesc(userEmail, pageable);
   }

   /**
    * Return all orders in the database
    * @param pageable
    * @return
    */
   public Page<Order> getAllOrders(Pageable pageable) {
      Pageable newPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").descending());
      return orderRepository.findAll(newPageable);
   }

   /**
    * Get all the items that user purchased in an order, given the orderId
    * @param orderId  the id of the order
    * @return
    */
   public List<OrderItem> getAllItemsInOrder(int orderId) {
      return orderItemRepository.findByOrderId(orderId);
   }


   /**
    * When user check out their cart, this service will be called
    * @param userEmail  the email of the user
    * @param cartItemList  the items in the cart
    * @throws GameNotFoundException
    */
   public boolean placeOrder(String userEmail, List<CartItem> cartItemList)
      throws GameNotFoundException{

      // check if any item has # > inventory
      for (CartItem cartItem : cartItemList) {
         int gameId = cartItem.getGameId();
         Optional<Game> gameOptional = gameService.findGameById(gameId);
         if(!gameOptional.isPresent()) {
            throw new GameNotFoundException("Game Not Found");
         }
         if(cartItem.getQuantity() > gameOptional.get().getNums()) {
            return false;
         }
      }

      Order order = orderRepository.save(new Order());
      order.setOrderStatus(PROCESSING);
      order.setUserEmail(userEmail);
      order.setReferNumber(OrderRefer.generateRefer(order.getId()));

      for(CartItem cartItem : cartItemList) {
         int gameId = cartItem.getGameId();
         Optional<Game> gameOptional = gameService.findGameById(gameId);
         if(!gameOptional.isPresent()) {
            throw new GameNotFoundException("Game Not Found");
         }
         // deduct inventory
         gameService.decreaseQuantity(gameId, cartItem.getQuantity());

         OrderItem orderItem = new OrderItem();
         orderItem.setGameId(gameId);
         orderItem.setPrice(gameOptional.get().getPrice());
         orderItem.setQuantity(cartItem.getQuantity());
         orderItem.setOrder(order);
         order.addOrderItem(orderItem);
      }
      orderRepository.save(order);
      return true;
   }

   /**
    * Admin complete an order
    * @param orderId
    * @throws OrderNotFoundException
    */
   public void completeOrder(int orderId) throws OrderNotFoundException{
      Optional<Order> orderOptional = orderRepository.findById(orderId);
      if(!orderOptional.isPresent()) {
         throw new OrderNotFoundException("Order Does Not Exist!");
      }
      Order order = orderOptional.get();
      order.setOrderStatus(COMPLETED);
      orderRepository.save(order);
   }

   /**
    * Admin cancel an order
    * @param orderId
    * @throws OrderNotFoundException
    */
   public void cancelOrder(int orderId) throws OrderNotFoundException{
      Optional<Order> orderOptional = orderRepository.findById(orderId);
      if(!orderOptional.isPresent()) {
         throw new OrderNotFoundException("Order Does Not Exist!");
      }
      Order order = orderOptional.get();
      order.setOrderStatus(CANCELED);
      // add inventory back
      for(OrderItem orderItem : order.getOrderItemList()) {
         gameService.increaseQuantity(orderItem.getGameId(), orderItem.getQuantity());
      }
      orderRepository.save(order);
   }


}
