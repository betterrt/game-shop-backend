package com.ryan.gameshop.service;

import com.ryan.gameshop.dao.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class CategoryService {
   private CategoryRepository categoryRepository;

   @Autowired
   public void setCategoryRepository(CategoryRepository categoryRepository) {
      this.categoryRepository = categoryRepository;
   }


}
