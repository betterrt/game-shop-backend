package com.ryan.gameshop.exception;

public class CategoryNotFoundException extends RuntimeException{
   public CategoryNotFoundException(String message) {
      super(message);
   }
}
