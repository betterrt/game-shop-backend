package com.ryan.gameshop.dao;

import com.ryan.gameshop.entity.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface GameRepository extends JpaRepository<Game, Integer> {
   Page<Game> findByNameContaining(String name, Pageable pageable);
   Page<Game> findByIdIn(List<Integer> ids, Pageable pageable);

}
