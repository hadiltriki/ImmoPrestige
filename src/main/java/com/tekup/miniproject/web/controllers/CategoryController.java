package com.tekup.miniproject.web.controllers;



import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tekup.miniproject.business.services.CategoryService;
import com.tekup.miniproject.dao.entities.Category;
import com.tekup.miniproject.web.models.CategoryForm;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    private static List<Category> categories = new ArrayList<Category>();
    private static Long idCount = 0L;
    public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/static/images";

   /* static {
        categories.add(new Category(++idCount, "Maison",  "men.png"));
        categories.add(new Category(++idCount, "Villa",  "women.png"));
        categories.add(new Category(++idCount, "Maison",  null));
        categories.add(new Category(++idCount, "Maison",  "men.png"));
    }*/
    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService){
      this.categoryService=categoryService;
    }

    @RequestMapping()
    public String getAllPerson(Model model) {
       
       model.addAttribute("categories", this.categoryService.getCategories());
        return "category-list";
    }


     @RequestMapping("/create")
    public String showAddCategoryForm(Model model) {
        model.addAttribute("categoryForm", new CategoryForm());
        return "add-category";
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public String addCategory(@Valid @ModelAttribute CategoryForm categoryForm,
            BindingResult bindingResult,
            Model model
          ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Invalid input");
            return "add-category";
        }
     
        this.categoryService.addCategory(new Category(null,categoryForm.getName(),categoryForm.getDescription(), null));

        
        return "redirect:/categories";
    }


    @RequestMapping("/{id}/edit")
    public String showEditCategoryForm(@PathVariable Long id, Model model) {
      
        Category category=this.categoryService.getCategoryById(id);
        model.addAttribute("categoryForm", new CategoryForm(category.getName(), category.getDescription()));
        model.addAttribute("id", id);
        return "edit-category";

        }

    @RequestMapping(path = "{id}/edit", method = RequestMethod.POST)
    public String editCategory(@Valid @ModelAttribute CategoryForm categoryForm,
            BindingResult bindingResult,
            @PathVariable Long id,
            Model model) {
        if (bindingResult.hasErrors()) {
            return "edit-category";
        }
      

        Category category=this.categoryService.getCategoryById(id);
        category.setName(categoryForm.getName());
        category.setDescription(categoryForm.getDescription());
       
        this.categoryService.updateCategory(category);
        return "redirect:/categories";
    }


    @RequestMapping(path = "{id}/delete", method = RequestMethod.POST)
    public String deleteCategory(@PathVariable Long id) {
       
        

         
        this.categoryService.deleteCategoryById(id);

        return "redirect:/categories";
    }






}
