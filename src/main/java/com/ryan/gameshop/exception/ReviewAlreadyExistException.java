package com.ryan.gameshop.exception;

public class ReviewAlreadyExistException extends RuntimeException{
   public ReviewAlreadyExistException (String message) {
      super(message);
   }
}
