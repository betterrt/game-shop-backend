package com.ryan.gameshop.controller;

import com.ryan.gameshop.entity.Review;
import com.ryan.gameshop.entity.request.ReviewRequest;
import com.ryan.gameshop.entity.response.PageResponse;
import com.ryan.gameshop.exception.ReviewAlreadyExistException;
import com.ryan.gameshop.exception.UserEmailMissingException;
import com.ryan.gameshop.service.ReviewService;
import com.ryan.gameshop.util.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/games")
@CrossOrigin("http://localhost:3000")
public class ReviewController {

   private ReviewService reviewService;

   @Autowired
   public ReviewController(ReviewService reviewService) {
      this.reviewService = reviewService;
   }

   @PostMapping("/review/{page}/{size}")
   public ResponseEntity<PageResponse> getReviewsByGameId(
         @PathVariable int page,
         @PathVariable int size,
         @RequestParam int gameId) {

      Pageable pageable = PageRequest.of(page, size);
      Page<Review> thePage = reviewService.findReviewsByGameId(gameId, pageable);

      return ResponseEntity.ok(PageResponse.builder()
            .totalPages(thePage.getTotalPages())
            .totalElements(thePage.getTotalElements())
            .dataList(thePage.getContent())
            .build());

   }

   @GetMapping("/secure/review/exist")
   public Boolean hasUserLeftAReview(
         @RequestHeader(value = "Authorization") String token,
         @RequestParam int gameId) throws UserEmailMissingException {

      String userEmail = ExtractJWT.extractUserEmail(token);
      if(userEmail == null) {
         throw new UserEmailMissingException("User Email Does Not Exist.");
      }
      return reviewService.hasUserLeftAReview(userEmail, gameId);

   }

   @PostMapping("/secure/review/create")
   public void createReview(
         @RequestHeader(value = "Authorization") String token,
         @RequestBody ReviewRequest reviewRequest)
         throws UserEmailMissingException, ReviewAlreadyExistException {

      String userEmail = ExtractJWT.extractUserEmail(token);
      if(userEmail == null) {
         throw new UserEmailMissingException("User Email Does Not Exist.");
      }
      reviewService.postReview(userEmail, reviewRequest);
   }

}
