package com.ryan.gameshop.exception;

import org.aspectj.weaver.ast.Not;

public class NotAdminException extends RuntimeException{
   public NotAdminException(String message) {
      super(message);
   }
}
