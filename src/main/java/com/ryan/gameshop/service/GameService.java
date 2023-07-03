package com.ryan.gameshop.service;

import com.ryan.gameshop.dao.CategoryRepository;
import com.ryan.gameshop.dao.GameCategoryMapRepository;
import com.ryan.gameshop.dao.GameRepository;
import com.ryan.gameshop.entity.Category;
import com.ryan.gameshop.entity.Game;
import com.ryan.gameshop.entity.GameCategoryMap;
import com.ryan.gameshop.exception.GameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class GameService {
   private GameRepository gameRepository;
   private CategoryRepository categoryRepository;
   private GameCategoryMapRepository gcRepository;

   @Autowired
   public void setGameRepository(GameRepository gameRepository) {
      this.gameRepository = gameRepository;
   }

   @Autowired
   public void setCategoryRepository(CategoryRepository categoryRepository) {
      this.categoryRepository = categoryRepository;
   }

   @Autowired
   public void setGcRepository(GameCategoryMapRepository gcRepository) {
      this.gcRepository = gcRepository;
   }

   /**
    * Return the game given its id
    * @param id
    * @return
    */
   public Optional<Game> findGameById(int id) {
      return gameRepository.findById(id);
   }

   /**
    * Return all the games with pagination
    * @param pageable  the parameters of pagination
    * @return
    */
   public Page<Game> findAllGames(Pageable pageable) {
      // return with pagination
      return gameRepository.findAll(pageable);
   }

   /**
    * Return all games that like 'name' with pagination
    * @param name  the search word
    * @param pageable  the parameters of pagination
    * @return
    */
   public Page<Game> findGamesByNameContaining(String name, Pageable pageable) {
      return gameRepository.findByNameContaining(name, pageable);
   }

   /**
    * Find all the categories of a game(the id of this game will be provided)
    * @param gameId  the id of the game
    * @return
    */
   public List<Category> findCategoriesByGame(int gameId) {
      List<GameCategoryMap> gcList = gcRepository.findByGameId(gameId);
      List<Category> categoryList = new ArrayList<>();
      for(GameCategoryMap gc : gcList) {
         Optional<Category> optionalCategory =
               categoryRepository.findById(gc.getCategoryId());
         if(!optionalCategory.isPresent()) {
            continue;
         }
         categoryList.add(optionalCategory.get());
      }
      return categoryList;
   }

   /**
    * Find all games of a category(the name of the category will be provided),
    * Return the answer with pagination.
    * @param category
    * @param pageable
    * @return
    */
   public Page<Game> findGamesByCategory(String category, Pageable pageable) {
      Optional<Category> optionalCategory = categoryRepository.findByName(category);
      if(!optionalCategory.isPresent()) {
         return Page.empty();
      }
      List<GameCategoryMap> gcList = gcRepository.
            findByCategoryId(optionalCategory.get().getId());
      List<Integer> gameIdList = new ArrayList<>();

      for(GameCategoryMap gc: gcList){
         gameIdList.add(gc.getGameId());
      }
      return gameRepository.findByIdIn(gameIdList, pageable);
   }

   /**
    * Add the quantity of the game by 1
    * @param gameId
    * @throws GameNotFoundException
    */
   public void increaseQuantity(int gameId, int num) throws GameNotFoundException{
      Optional<Game> gameOptional = gameRepository.findById(gameId);
      if(!gameOptional.isPresent()) {
         throw new GameNotFoundException("Game Not Found");
      }
      Game game = gameOptional.get();
      game.setNums(game.getNums() + num);
      gameRepository.save(game);
   }

   /**
    * Decrease the quantity of the game by 1
    * @param gameId
    * @throws GameNotFoundException
    */
   public Boolean decreaseQuantity(int gameId, int num) throws GameNotFoundException{
      Optional<Game> gameOptional = gameRepository.findById(gameId);
      if(!gameOptional.isPresent()) {
         throw new GameNotFoundException("Game Not Found");
      }
      Game game = gameOptional.get();
      if(game.getNums() - num < 0) {
         return false;
      }
      game.setNums(game.getNums() - num);
      gameRepository.save(game);
      return true;

   }

   /**
    * Update the price of the game
    * @param gameId
    * @param price
    * @throws GameNotFoundException
    */
   public void updatePrice(int gameId, double price) throws GameNotFoundException {
      Optional<Game> gameOptional = gameRepository.findById(gameId);
      if(!gameOptional.isPresent()) {
         throw new GameNotFoundException("Game Not Found");
      }
      Game game = gameOptional.get();
      game.setPrice(price);
      gameRepository.save(game);
   }
}
