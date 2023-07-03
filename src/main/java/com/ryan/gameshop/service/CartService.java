package com.ryan.gameshop.service;

import com.ryan.gameshop.dao.CartItemRepository;
import com.ryan.gameshop.dao.CartRepository;
import com.ryan.gameshop.entity.Cart;
import com.ryan.gameshop.entity.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;


@Service
@Transactional
public class CartService {
   private CartRepository cartRepository;
   private CartItemRepository cartItemRepository;

   @Autowired
   public void setCartRepository(CartRepository cartRepository) {
      this.cartRepository = cartRepository;
   }

   @Autowired
   public void setCartItemRepository(CartItemRepository cartItemRepository) {
      this.cartItemRepository = cartItemRepository;
   }

   /**
    * User add a game into his cart, or increase the quantity of the game in his cart
    * @param userEmail the email of user
    * @param gameId  the id of game
    */
   public void addGameToCart(String userEmail, int gameId) {
      // if user doesn't have a cart, create one for him.
      if(!cartRepository.findByUserEmail(userEmail).isPresent()) {
         Cart cart = new Cart();
         cart.setUserEmail(userEmail);
         cartRepository.save(cart);
      }

      Cart cart = cartRepository.findByUserEmail(userEmail).get();

      Optional<CartItem> itemOptional = cartItemRepository
            .findByCartIdAndGameId(cart.getId(), gameId);

      // if game wasn't in the cart
      if(!itemOptional.isPresent()) {
         CartItem cartItem = new CartItem();
         cartItem.setGameId(gameId);
         cartItem.setCart(cart);
         cartItem.setQuantity(1);
         // Here we only need to do save once, because we have CascadeType.ALL
         cart.addCartItem(cartItem);
      }
      // if game already in the cart(user increase quantity)
      else {
         CartItem cartItem = itemOptional.get();
         cartItem.setQuantity(cartItem.getQuantity() + 1);
      }
      cartRepository.save(cart);
   }

   /**
    * Subtract the quantity of an item in the game. If quantity==1, then just remove
    * this game from the cart
    * @param cartItemId  the id of the cart
    */
   public void subtractGameFromCart(int cartItemId) {
      Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartItemId);

      if(cartItemOptional.isPresent()) {
         CartItem cartItem = cartItemOptional.get();

         if(cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            cartItemRepository.save(cartItem);
         } else {
            cartItemRepository.deleteById(cartItemId);
         }
      }
   }

   /**
    * Remove a game from the cart. No matter how many copies it has.
    * @param cartItemId  the id of the item
    */
   public void removeGameFromCart(int cartItemId) {
      cartItemRepository.deleteById(cartItemId);
   }

   /**
    * Delete the cart of the user given userEmail.
    * Can happen when a user place an order, or the user remove all the items in his cart.
    * @param userEmail the email of user
    */
   public void deleteCart(String userEmail) {
      cartRepository.deleteByUserEmail(userEmail);
   }

   /**
    * Return all the items in the cart
    * @param userEmail  the email of the user
    * @return
    */
   public List<CartItem> getAllItemsInCart(String userEmail) {
      Optional<Cart> cartOptional = cartRepository.findByUserEmail(userEmail);
      if(!cartOptional.isPresent()) {
         return new ArrayList<>();
      }
      Cart cart = cartOptional.get();
      return cartItemRepository.findByCartId(cart.getId());
   }

   /**
    * Return the quantity of the game in the use's cart
    * @param userEmail  the email of the user
    * @param gameId  the id of the game
    * @return
    */
   public int findGameQuantityInCart(String userEmail, int gameId) {
      // if user doesn't have a cart, then cart must be empty
      Optional<Cart> cartOptional = cartRepository.findByUserEmail(userEmail);
      if(!cartOptional.isPresent()) {
         return 0;
      }

      Cart cart = cartOptional.get();
      Optional<CartItem> itemOptional = cartItemRepository
            .findByCartIdAndGameId(cart.getId(), gameId);

      // if game wasn't in the cart
      if(!itemOptional.isPresent()) {
         return 0;
      }
      // if game is in the cart
      return itemOptional.get().getQuantity();
   }
}
