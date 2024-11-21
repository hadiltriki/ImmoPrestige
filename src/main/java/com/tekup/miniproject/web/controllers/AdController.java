package com.tekup.miniproject.web.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.tekup.miniproject.business.services.AdService;
import com.tekup.miniproject.business.services.CategoryService;
import com.tekup.miniproject.dao.entities.Ad;
import com.tekup.miniproject.dao.entities.Category;
import com.tekup.miniproject.dao.entities.Photo;
import com.tekup.miniproject.dao.repositories.PhotoRepository;
import com.tekup.miniproject.web.models.AdForm;


import jakarta.validation.Valid;

import java.io.IOException;
@Controller
@RequestMapping("/ads")
public class AdController {
    private static List<Ad> ads = new ArrayList<Ad>();
    private static Long idCount = 0L;
    public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/static/images";
    @Autowired
    private final AdService adService;
    @Autowired
    private final CategoryService categoryService;

    @Autowired
    private PhotoRepository photoRepository;
    public AdController(AdService adService, CategoryService categoryService){
      this.adService=adService;
      this.categoryService=categoryService;
    }
     @RequestMapping()
    public String getAllPerson(Model model) {
       
       model.addAttribute("ads", this.adService.getAds());
        return "ad-list";
    }
     @RequestMapping("/createAd")
    public String showAddAdForm(Model model) {
        model.addAttribute("adForm", new AdForm());
        model.addAttribute("categories", this.categoryService.getCategories());
        return "add-ad";
    }
    @RequestMapping(path = "/createAd", method = RequestMethod.POST)
    public String addAd(@Valid @ModelAttribute AdForm adForm,
                        BindingResult bindingResult,
                        @RequestParam("photos") List<MultipartFile> files,
                        Model model) throws IOException{
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Invalid input");
            return "add-ad";
        }
        List<Photo> photos = new ArrayList<>();
        
     
       
        
    
        // Créer une nouvelle annonce
        Ad ad = new Ad();
        ad.setTitle(adForm.getTitle());
        ad.setDescription(adForm.getDescription());
        ad.setLocation(adForm.getLocation());
        ad.setContact(adForm.getContact());
        ad.setPrice(adForm.getPrice());
        ad.setNumberOfRooms(adForm.getNumberOfRooms());
        ad.setArea(adForm.getArea());
        ad.setPhotos(photos);
        Category category = categoryService.getCategoryById(adForm.getCategoryId());
        ad.setCategory(category);
        try { 
            adService.addAd(ad);  // Sauvegarde de l'annonce pour générer l'ID
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de l'ajout de l'annonce: " + e.getMessage());
            System.out.println("Erreur lors de l'ajout de l'annonce : " + e.getMessage());
    
            // Retourner la vue de création d'annonce en cas d'erreur
            return "add-ad";
             
        }
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                Photo photo = new Photo();
                photo.setChemin(file.getOriginalFilename());
                photo.setAd(ad);
                photoRepository.save(photo);
                photos.add(photo);
            }
        }
        ad.setPhotos(photos);
        adService.updateAd(ad);
    
        // Récupérer la catégorie de l'annonce
       
    
        // Enregistrer l'annonce dans la base de données
        

    
       
        // Rediriger vers la liste des annonces après l'ajout
        return "redirect:/ads";
    }
    
    @RequestMapping("/{id}/editAd")
    public String showEditAdForm(@PathVariable Long id, Model model) {
        Ad ad = this.adService.getAdById(id);
    
        if (ad == null) {
            model.addAttribute("errorMessage", "Ad not found.");
            return "redirect:/ads";
        }
    
        model.addAttribute("adForm", new AdForm(
            ad.getTitle(),
            ad.getDescription(),
            ad.getLocation(),
            ad.getContact(),
            ad.getPrice(),
            ad.getNumberOfRooms(),
            ad.getArea(),
            ad.getCategory().getId(),
            null
        ));
        model.addAttribute("id", id);
        model.addAttribute("categories", this.categoryService.getCategories());
        return "edit-ad";
    }
    
    @RequestMapping(path = "{id}/editAd", method = RequestMethod.POST)
    public String editAd(
            @Valid @ModelAttribute AdForm adForm,
            BindingResult bindingResult,
            @PathVariable Long id,
            Model model) {
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", this.categoryService.getCategories());
            return "edit-ad";
        }
    
        Ad ad = this.adService.getAdById(id);
        if (ad == null) {
            model.addAttribute("errorMessage", "Ad not found.");
            return "redirect:/ads";
        }
    
        ad.setTitle(adForm.getTitle());
        ad.setDescription(adForm.getDescription());
        ad.setLocation(adForm.getLocation());
        ad.setContact(adForm.getContact());
        ad.setPrice(adForm.getPrice());
        ad.setArea(adForm.getArea());
        ad.setNumberOfRooms(adForm.getNumberOfRooms());
    
        Category category = this.categoryService.getCategoryById(adForm.getCategoryId());
        if (category == null) {
            model.addAttribute("errorMessage", "Category not found.");
            model.addAttribute("categories", this.categoryService.getCategories());
            return "edit-ad";
        }
        ad.setCategory(category);
    
        try {
            this.adService.updateAd(ad);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to update ad: " + e.getMessage());
            model.addAttribute("categories", this.categoryService.getCategories());
            return "edit-ad";
        }
    
        return "redirect:/ads";
    }
    


    @RequestMapping(path = "{id}/deleteAd", method = RequestMethod.POST)
    public String deleteAd(@PathVariable Long id) {
       
        

         
        this.adService.deleteAdById(id);

        return "redirect:/ads";
    }


}
