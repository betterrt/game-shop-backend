package com.ryan.gameshop.exception;

public class GameNotFoundException extends RuntimeException{
   public GameNotFoundException(String message) {
      super(message);
   }
}
