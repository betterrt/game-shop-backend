package com.ryan.gameshop.service;

import com.ryan.gameshop.dao.CategoryRepository;
import com.ryan.gameshop.dao.GameCategoryMapRepository;
import com.ryan.gameshop.dao.GameRepository;
import com.ryan.gameshop.entity.Category;
import com.ryan.gameshop.entity.Game;
import com.ryan.gameshop.entity.GameCategoryMap;
import com.ryan.gameshop.entity.request.AddGameRequest;
import com.ryan.gameshop.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class AdminService {
   private GameRepository gameRepository;
   private GameCategoryMapRepository gcRepository;
   private CategoryRepository categoryRepository;

   @Autowired
   public void setGameRepository(GameRepository gameRepository) {
      this.gameRepository = gameRepository;
   }

   @Autowired
   public void setGcRepository(GameCategoryMapRepository gcRepository) {
      this.gcRepository = gcRepository;
   }

   @Autowired
   public void setCategoryRepository(CategoryRepository categoryRepository) {
      this.categoryRepository = categoryRepository;
   }

   /**
    * Used by admin to add new game into the shop
    * @param request  the request that contains info of the new game
    * @throws CategoryNotFoundException
    */
   public void addNewGame(AddGameRequest request) throws CategoryNotFoundException{
      Game game = new Game();
      game.setName(request.getName());
      // Parse date
      SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
      try {
         Date date = dateFormat.parse(request.getReleasedDate());
         game.setReleasedDate(date);
      } catch (ParseException e) {
         System.out.println("Failed to parse date.");
      }

      game.setDeveloper(request.getDeveloper());
      game.setPublisher(request.getPublisher());
      game.setDescription(request.getDescription());
      game.setNums(request.getNums());
      game.setPrice(request.getPrice());
      game.setImage(request.getImage());
      Game savedGame = gameRepository.save(game);

      // set category
      GameCategoryMap gameCategoryMap = new GameCategoryMap();
      gameCategoryMap.setGameId(savedGame.getId());
      for(String categoryName : request.getCategories()) {
         Optional<Category> categoryOptional = categoryRepository.findByName(categoryName);
         if(!categoryOptional.isPresent()) {
            throw new CategoryNotFoundException("Category Not Found!");
         }
         gameCategoryMap.setCategoryId(categoryOptional.get().getId());

      }
      gcRepository.save(gameCategoryMap);
   }


}
