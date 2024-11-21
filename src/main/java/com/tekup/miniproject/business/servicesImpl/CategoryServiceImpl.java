package com.tekup.miniproject.business.servicesImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tekup.miniproject.business.services.CategoryService;
import com.tekup.miniproject.dao.entities.Category;
import com.tekup.miniproject.dao.repositories.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    public CategoryServiceImpl(CategoryRepository categoryRepository){
     this.categoryRepository=categoryRepository;
    }
    @Override
    public List<Category> getCategories() {
        return this.categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
       return this.categoryRepository.findById(id).get();
    }

    @Override
    public List<Category> getCategorybyName(String name) {
        return this.categoryRepository.findByName(name);
    }

    @Override
    public Category addCategory(Category category) {
        if(category==null){
            return null;
        }
       return this.categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category) {
        if(category==null){
            return null;
        }
       return this.categoryRepository.save(category); 
    }

    @Override
    public void deleteCategoryById(Long id) {
        if(id==null){
            return ;
        }
         this.categoryRepository.deleteById(id);
    }

}
