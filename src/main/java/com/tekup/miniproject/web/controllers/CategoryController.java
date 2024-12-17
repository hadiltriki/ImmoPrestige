package com.tekup.miniproject.web.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tekup.miniproject.business.services.CategoryService;
import com.tekup.miniproject.dao.entities.Category;
import com.tekup.miniproject.web.models.CategoryForm;

import jakarta.validation.Valid;
/*
 * CRUD operations Map
 * Create : /categories/create (Get: pour récupérer le fomulaire d'ajout d'une nouvelle catégorie)
 *                          (Post: pour ajouter une catégorie la table  Categories)
 * Read : /categories (Get)
 *      
 * 
 * Update : /categories/{id}/edit (Get: pour récuperer le formulaire de modification une nouvelle catégorie ) 
 *                             (Post : pour modifier une catégorie par son id)    
 * Delelte: /categories/{id}/delete (Post : permet de supprimer une catégorie  par son id)                     
 */

@Controller
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public String getAllPerson(Model model) {
        List<Category> categories = categoryService.getCategories();
        model.addAttribute("categories", categories);
        return "category-list";
    }

    @GetMapping("/create")
    public String showAddCategoryForm(Model model) {
        CategoryForm categoryForm = new CategoryForm();
        model.addAttribute("categoryForm", categoryForm);
        return "add-category";
    }

    @PostMapping(path = "/create")
    public String addCategory(@Valid @ModelAttribute("categoryForm") CategoryForm categoryForm,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Invalid input");
            return "add-category";
        }

        categoryService.addCategory(new Category(null, categoryForm.getName(), categoryForm.getDescription(), null));

        return "redirect:/categories";// Rediriger vers la liste des catégories l'ajout
    }

    @GetMapping("/{id}/edit")
    public String showEditCategoryForm(@PathVariable Long id, Model model) {

        Category category = categoryService.getCategoryById(id);
        model.addAttribute("categoryForm", new CategoryForm(category.getName(), category.getDescription()));
        model.addAttribute("id", category.getId());
        return "edit-category";

    }

    @PostMapping("{id}/edit")
    public String editCategory(@PathVariable Long id,
            @Valid @ModelAttribute("categoryForm") CategoryForm categoryForm,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit-category";
        }
        categoryService.updateCategory(id,
                new Category(null, categoryForm.getName(), categoryForm.getDescription(), null));
        return "redirect:/categories"; // Rediriger vers la liste des catégories après la mise à jour

    }

    @PostMapping("{id}/delete")
    public String deleteCategory(@PathVariable Long id) {

        categoryService.deleteCategoryById(id);

        return "redirect:/categories";
    }

}
