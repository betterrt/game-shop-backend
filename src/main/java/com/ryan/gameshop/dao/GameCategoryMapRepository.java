package com.ryan.gameshop.dao;

import com.ryan.gameshop.entity.GameCategoryMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GameCategoryMapRepository extends JpaRepository<GameCategoryMap, Integer> {
   List<GameCategoryMap> findByGameId(int gameId);
   List<GameCategoryMap> findByCategoryId(int categoryId);
}
