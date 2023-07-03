package com.ryan.gameshop.exception;


public class UserEmailMissingException extends RuntimeException{
   public UserEmailMissingException(String message) {
      super(message);
   }
}
