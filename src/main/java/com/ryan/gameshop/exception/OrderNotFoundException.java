package com.ryan.gameshop.exception;

public class OrderNotFoundException extends RuntimeException{
   public OrderNotFoundException(String message) {
      super(message);
   }
}
