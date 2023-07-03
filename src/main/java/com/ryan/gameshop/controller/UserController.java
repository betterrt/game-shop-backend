package com.ryan.gameshop.controller;

import com.ryan.gameshop.entity.Category;
import com.ryan.gameshop.entity.Game;
import com.ryan.gameshop.entity.response.PageResponse;
import com.ryan.gameshop.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/games")
@CrossOrigin("http://localhost:3000")
public class UserController {
   private GameService gameService;

   @Autowired
   public UserController(GameService gameService) {
      this.gameService = gameService;
   }

   @GetMapping("/{page}/{size}")
   public ResponseEntity<PageResponse> getGames(@PathVariable int page,
                                                @PathVariable int size) {

      Pageable pageable = PageRequest.of(page, size);
      Page<Game> thePage = gameService.findAllGames(pageable);

      return ResponseEntity.ok(PageResponse.builder()
            .totalPages(thePage.getTotalPages())
            .totalElements(thePage.getTotalElements())
            .dataList(thePage.getContent())
            .build());
   }

   @GetMapping("/gameId/{gameId}")
   public ResponseEntity<Game> getGameById(@PathVariable int gameId) {
      Optional<Game> gameOptional = gameService.findGameById(gameId);
      if(!gameOptional.isPresent()) {
         return new ResponseEntity<>(new Game(), HttpStatus.NOT_FOUND);
      }
      return ResponseEntity.ok(gameOptional.get());
   }

   @PostMapping("/categories")
   public ResponseEntity<List<String>> findCategoriesByGame(@RequestParam int gameId) {
      List<Category> list = gameService.findCategoriesByGame(gameId);
      List<String> responseList = new ArrayList<>();
      for(Category category : list) {
         responseList.add(category.getName());
      }
      return ResponseEntity.ok(responseList);
   }

   // example: http://localhost:8080/games/search/findByNameContaining/0/2/?name=COD
   @PostMapping("/search/findByNameContaining/{page}/{size}")
   public ResponseEntity<PageResponse> getGamesByNameContaining(
         @PathVariable int page,
         @PathVariable int size,
         @RequestParam String name) {

      Pageable pageable = PageRequest.of(page, size);
      Page<Game> thePage = gameService.findGamesByNameContaining(name, pageable);

      return ResponseEntity.ok(PageResponse.builder()
            .totalPages(thePage.getTotalPages())
            .totalElements(thePage.getTotalElements())
            .dataList(thePage.getContent())
            .build());
   }

   @PostMapping("/search/findByCategory/{page}/{size}")
   public ResponseEntity<PageResponse> getGamesByCategory(
         @PathVariable int page,
         @PathVariable int size,
         @RequestParam String category) {

      Pageable pageable = PageRequest.of(page, size);
      Page<Game> thePage = gameService.findGamesByCategory(category, pageable);

      return ResponseEntity.ok(PageResponse.builder()
            .totalPages(thePage.getTotalPages())
            .totalElements(thePage.getTotalElements())
            .dataList(thePage.getContent())
            .build());
   }


}
