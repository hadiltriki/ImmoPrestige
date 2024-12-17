package com.tekup.miniproject.business.servicesImpl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.tekup.miniproject.business.services.CategoryService;
import com.tekup.miniproject.dao.entities.Category;
import com.tekup.miniproject.dao.repositories.CategoryRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
      if(id == null)
      {
        return null;
      }
      return this.categoryRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Category with id : "+ id + " not found!"));
    }

    @Override
    public List<Category> getCategorybyName(String name) {
        return this.categoryRepository.findByName(name);
    }

    @Override
    public Category addCategory(Category category) {
      /*  if(category==null){
            return null;
        }
       Category newCategory= new Category();
       try{
        newCategory= categoryRepository.save(category);

       } catch(DataIntegrityViolationException e)
       {
        log.error(e.getMessage());
       }
       return newCategory;*/
       if(category==null){
        return null;
    }
   return this.categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long id,Category category) {
       Category categoryExisting= this.getCategoryById(id);
       categoryExisting.setName(category.getName());
       categoryExisting.setDescription(category.getDescription());
       return categoryRepository.save(categoryExisting);
    }

    @Override
    public void deleteCategoryById(Long id) {
        if(id==null){
            return ;
        }
        else if(this.categoryRepository.existsById(id))
        {
            this.categoryRepository.deleteById(id);
        }
        else{
            throw new EntityNotFoundException("Category with id: "+id+ " not found");
        }
         
    }

}
