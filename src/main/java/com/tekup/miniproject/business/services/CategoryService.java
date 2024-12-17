package com.tekup.miniproject.business.services;

import java.util.List;

import com.tekup.miniproject.dao.entities.Category;

public interface CategoryService {
    List<Category> getCategories();
    Category getCategoryById(Long id);
    List<Category> getCategorybyName(String name);
    //create
    Category addCategory(Category category);
    //Update
    Category updateCategory(Long id,Category category);
    //Delete
    void deleteCategoryById(Long id);

}
