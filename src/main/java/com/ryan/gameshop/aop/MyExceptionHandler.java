package com.ryan.gameshop.aop;

import com.ryan.gameshop.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@ControllerAdvice
public class MyExceptionHandler {

   @ExceptionHandler(value = {CategoryNotFoundException.class})
   public ResponseEntity<String> handleCategoryNotFound(
         CategoryNotFoundException e) {

      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
   }

   @ExceptionHandler(value = {GameNotFoundException.class})
   public ResponseEntity<String> handleGameNotFound (
         GameNotFoundException e) {

      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
   }

   @ExceptionHandler(value = {NotAdminException.class})
   public ResponseEntity<String> handleNotAdmin (
         NotAdminException e) {

      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
   }
   @ExceptionHandler(value = {OrderNotFoundException.class})
   public ResponseEntity<String> handleOrderNotFound (
         OrderNotFoundException e) {

      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
   }
   @ExceptionHandler(value = {ReviewAlreadyExistException.class})
   public ResponseEntity<String> handleReviewAlreadyExistException (
         ReviewAlreadyExistException e) {

      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
   }

   @ExceptionHandler(value = {UserEmailMissingException.class})
   public ResponseEntity<String> handleUserEmailMissing (
         UserEmailMissingException e) {

      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
   }

}

