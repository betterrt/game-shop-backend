package com.ryan.gameshop.service;

import com.ryan.gameshop.dao.ReviewRepository;
import com.ryan.gameshop.entity.Review;
import com.ryan.gameshop.entity.request.ReviewRequest;
import com.ryan.gameshop.exception.ReviewAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewService {

   private ReviewRepository reviewRepository;

   @Autowired
   public void setReviewRepository(ReviewRepository reviewRepository) {
      this.reviewRepository = reviewRepository;
   }

   /**
    * Return reviews by descending order of date with pagination
    * @param gameId  the id of the game
    * @param pageable  the pagination info
    * @return
    */
   public Page<Review> findReviewsByGameId(int gameId, Pageable pageable) {
      return reviewRepository.findByGameIdOrderByReviewDateDesc(gameId, pageable);
   }

   /**
    * Post the new review created by user to the database. If user already has a review
    * for the game, throw an exception
    * @param userEmail  the email of current user
    * @param reviewRequest  the review that user left
    * @throws ReviewAlreadyExistException
    */
   public void postReview(String userEmail, ReviewRequest reviewRequest)
         throws ReviewAlreadyExistException{

      Optional<Review> optionalReview = reviewRepository.
            findByGameIdAndUserEmail(reviewRequest.getGameId(), userEmail);

      if(optionalReview.isPresent()) {
         throw new ReviewAlreadyExistException("Review Already Exist.");
      }
      Review review = new Review();
      review.setGameId(reviewRequest.getGameId());
      review.setUserEmail(userEmail);
      review.setRating(reviewRequest.getRating());

      Optional<String> optionalDes = reviewRequest.getReviewDescription();
      if(optionalDes.isPresent()) {
         review.setReviewDescription(optionalDes.get());
      }
      reviewRepository.save(review);
   }

   /**
    * Check if the user with userEmail has left a review of the game
    * @param userEmail  the email of current user
    * @param gameId  the ID of the game
    * @return
    */
   public Boolean hasUserLeftAReview(String userEmail, int gameId) {
      Optional<Review> optionalReview = reviewRepository
            .findByGameIdAndUserEmail(gameId, userEmail);

      return optionalReview.isPresent() ? true : false;
   }
}
