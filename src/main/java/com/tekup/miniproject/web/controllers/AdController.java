package com.tekup.miniproject.web.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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

import com.tekup.miniproject.web.models.AdForm;

import jakarta.validation.Valid;
/* Carte des opérations CRUD
 * 
 * Create - Get  /ads/create : Récupérer le forumulaire d'ajout d'une nouvelle annonce
 *        - Post /ads/create : Ajouter une annonce à la liste annonces  
 * 
 * Read   - Get /ads :       Récupérer une liste de annonces
 * Read   - Get /ads?page=<num_page>&pageSize=<nbre_annonce> :       Récupérer une liste de personnes
 * 
 * Read with filter : /annonces/filter?sortByTitle=<asc|desc>
 *     GET  -> Trier la liste des annonces par titre (ascendant ou descendant)
 *                     /ads/filter?sortByTiltle=<asc|desc>&page=<num_page>&pageSize=<nbre_annonce>
 * Update  -Get /ads/{id}/edit  : Récuprer le formulaire de mise à jour de annonce
 *         -Post /ads/{id}/edit : Mettre à jour une annonce dans la liste annonces
 * 
 * Delete -Post /ads/{id}/delete  : supprimer une annonce de la liste annonces
 */

@Controller
@RequestMapping("/ads")
public class AdController {

    public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/static/images";
    @Autowired
    private final AdService adService;
    @Autowired
    private final CategoryService categoryService;

    public AdController(AdService adService, CategoryService categoryService) {
        this.adService = adService;
        this.categoryService = categoryService;
    }


    @RequestMapping("/home")
public String getThreePersons(Model model) {
    List<Ad> ads = this.adService.getAds(); // Récupérer toutes les annonces
    int adsCount = ads.size(); // Nombre d'annonces dans la liste
    
    // Condition pour limiter le nombre d'annonces affichées
    if (adsCount == 2) {
        ads = ads.stream().limit(2).collect(Collectors.toList()); // Afficher 2 annonces
    } else if (adsCount >= 3) {
        ads = ads.stream().limit(3).collect(Collectors.toList()); // Afficher 3 annonces
    } else {
        ads = new ArrayList<>(); // Ne rien afficher si la taille est inférieure à 1 ou autre condition
    }

    model.addAttribute("ads", ads); 
    
    return "homeP";  // Retourner la vue avec la liste des annonces
}





   /* @RequestMapping()
    public String getAllPerson( @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "3") int pageSize
    ,Model model) {
        Page<Ad> adPage = this.adService.getAllAdPagination(PageRequest.of(page, pageSize));
      
        model.addAttribute("ads", adPage.getContent());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", adPage.getTotalPages());
       return "ad-list";
    } */
    
  @RequestMapping()
    public String getAllPersons(Model model) {
        List<Ad> ads = this.adService.getAds();
         OptionalDouble minPrice = ads.stream().mapToDouble(Ad::getPrice).min(); 
    OptionalDouble maxPrice = ads.stream().mapToDouble(Ad::getPrice).max(); 
    OptionalDouble minArea = ads.stream().mapToDouble(Ad::getArea).min(); 
    OptionalDouble maxArea = ads.stream().mapToDouble(Ad::getArea).max(); 
    List<Integer> distinctRooms = ads.stream()
    .map(Ad::getNumberOfRooms)      // Récupérer le nombre de chambres
    .distinct()                     // Garder les valeurs distinctes
    .sorted()                       // Trier les valeurs
    .limit(4)                       // Limiter à 4
    .collect(Collectors.toList());  // Collecter dans une liste

// Récupérer la 4ème valeur (si elle existe)
Integer roomsPlus = distinctRooms.size() == 4 ? distinctRooms.get(3) : null;

// Ajouter la liste au modèle pour l'utiliser dans la vue
model.addAttribute("distinctRooms", distinctRooms);
model.addAttribute("roomsPlus", roomsPlus);

    model.addAttribute("minPrice", minPrice.isPresent() ? minPrice.getAsDouble() : 0); 
    model.addAttribute("maxPrice", maxPrice.isPresent() ? maxPrice.getAsDouble() : 0); 
    model.addAttribute("minArea", minArea.isPresent() ? minArea.getAsDouble() : 0); 
    model.addAttribute("maxArea", maxArea.isPresent() ? maxArea.getAsDouble() : 0); 
  
        model.addAttribute("ads", this.adService.getAds());
        model.addAttribute("categories", this.categoryService.getCategories());
         return "catalogue";
     }
 


    @RequestMapping("/filter")
    public String getAdSorted(@RequestParam(required = false, defaultValue = "asc") String sortByPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int pageSize,
            Model model) {
                Page<Ad> adPage = this.adService.getAdSortedByPricePagination(sortByPrice,
                PageRequest.of(page, pageSize));
        model.addAttribute("ads", adPage.getContent());
        model.addAttribute("sortByPrice", sortByPrice);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", adPage.getTotalPages());
        return "ad-list";
    }

    @RequestMapping("/create")
    public String showAddAdForm(Model model) {
        model.addAttribute("adForm", new AdForm());
        model.addAttribute("categories", this.categoryService.getCategories());
        return "add-ad";
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public String addAd(@Valid @ModelAttribute AdForm adForm,
            BindingResult bindingResult,
            @RequestParam MultipartFile file,
            Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Invalid input");
            model.addAttribute("categories", this.categoryService.getCategories());
           
            return "add-ad";
        }
        Ad ad = new Ad();
        ad.setTitle(adForm.getTitle());
        ad.setDescription(adForm.getDescription());
        ad.setLocation(adForm.getLocation());
        ad.setContact(adForm.getContact());
        ad.setPrice(adForm.getPrice());
        ad.setNumberOfRooms(adForm.getNumberOfRooms());
        ad.setArea(adForm.getArea());
        ad.setAdType(adForm.getAdType());  
        Category category = categoryService.getCategoryById(adForm.getCategoryId());
        ad.setCategory(category);

        if (!file.isEmpty()) {
            StringBuilder fileName = new StringBuilder();
            fileName.append(file.getOriginalFilename());
            Path newFilePath = Paths.get(uploadDirectory, fileName.toString());

            try {
                Files.write(newFilePath, file.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                ad.setPhoto(fileName.toString());
                adService.addAd(ad); // Sauvegarde de l'annonce pour générer l'ID
            } catch (Exception e) {
                model.addAttribute("errorMessage", "Erreur lors de l'ajout de l'annonce: " + e.getMessage());
                System.out.println("Erreur lors de l'ajout de l'annonce : " + e.getMessage());
                model.addAttribute("categories", this.categoryService.getCategories());
               
    
                // Retourner la vue de création d'annonce en cas d'erreur
                return "add-ad";

            }

        } else {

            try {
                ad.setPhoto(null);
                adService.addAd(ad); // Sauvegarde de l'annonce pour générer l'ID
            } catch (Exception e) {
                model.addAttribute("errorMessage", "Erreur lors de l'ajout de l'annonce: " + e.getMessage());
                System.out.println("Erreur lors de l'ajout de l'annonce : " + e.getMessage());
                model.addAttribute("categories", this.categoryService.getCategories());
                // Retourner la vue de création d'annonce en cas d'erreur
                return "add-ad";

            }

        }

        return "redirect:/ads/home";
    }

    @RequestMapping("/{id}/edit")
    public String showEditAdForm(@PathVariable Long id, Model model) {
        Ad ad = this.adService.getAdById(id);

        model.addAttribute("adForm", new AdForm(
                ad.getTitle(),
                ad.getDescription(),
                ad.getLocation(),
                ad.getContact(),
                ad.getPrice(),
                ad.getNumberOfRooms(),
                ad.getArea(),
                ad.getCategory().getId(),ad.getPhoto(), ad.isFavoris(), ad.getAdType() 
                /* if ad(getbyid) exist in favoris where user= userConnecté */
        ));
        model.addAttribute("id", id);
       
        model.addAttribute("categories", this.categoryService.getCategories());
        
        return "edit-ad";
    }

    @RequestMapping(path = "{id}/edit", method = RequestMethod.POST)
    public String editAd(
            @Valid @ModelAttribute AdForm adForm,
            BindingResult bindingResult,
            @PathVariable Long id,
            Model model,
            @RequestParam MultipartFile file) {

        if (bindingResult.hasErrors()) {
            
            return "edit-ad";
        }

        Ad ad = this.adService.getAdById(id);

        ad.setTitle(adForm.getTitle());
        ad.setDescription(adForm.getDescription());
        ad.setLocation(adForm.getLocation());
        ad.setContact(adForm.getContact());
        ad.setPrice(adForm.getPrice());
        ad.setArea(adForm.getArea());
        ad.setNumberOfRooms(adForm.getNumberOfRooms());
        ad.setFavoris(adForm.isFavoris());
        ad.setAdType(adForm.getAdType());
        Category category = this.categoryService.getCategoryById(adForm.getCategoryId());
        ad.setCategory(category);
    

        if (!file.isEmpty()) {
            // upload photo
            StringBuilder fileName = new StringBuilder();
            Path newFilePath = Paths.get(uploadDirectory, file.getOriginalFilename());
            fileName.append(file.getOriginalFilename());
            try {
                Files.write(newFilePath, file.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
            // supprimer ancienne photo
            if (ad.getPhoto() != null) {
                Path filePath = Paths.get(uploadDirectory, ad.getPhoto());
                try {
                    Files.deleteIfExists(filePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ad.setPhoto(fileName.toString());
        }

   
            this.adService.updateAd(id, ad);
        
        return "redirect:/ads";
    }

    @RequestMapping(path = "{id}/delete", method = RequestMethod.POST)
    public String deleteAd(@PathVariable Long id) {
        Ad ad = this.adService.getAdById(id);
        // Supprimer le fichier de photo si existe
        if (ad.getPhoto() != null) {
            Path filePath = Paths.get(uploadDirectory, ad.getPhoto());
            try {
                Files.deleteIfExists(filePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.adService.deleteAdById(id);

        return "redirect:/ads";
    }

    @GetMapping("/search")
    public String searchAds(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) Integer rooms,
            @RequestParam(required = false) String contact,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String adType,
            Model model) {

        List<Ad> results;
 
        if (location != null && !location.isEmpty()) {
            results =adService.getAdByLocation(location);
        } else if (price != null) {
            results = adService.getAdByPrice(price);
        } else if (rooms != null) {
            results = adService.getAdByRooms(rooms);
        } else if (category != null && !category.isEmpty()) {
            results = adService.getAdByCategory(category);
        }else if (adType != null && !adType.isEmpty()) { // Filtrer par type
            results = adService.getAdByType(adType); 
        }
        else if (contact != null && !contact.isEmpty()) {
            results = adService.getAdByContact(contact);
        } else {
            results = adService.getAds();
        }

        model.addAttribute("ads", results);
        return "ad-list";
        
    }

    @RequestMapping(path = "{id}/addFavoris", method = RequestMethod.POST)
    public String addFavoris(
            @Valid @ModelAttribute AdForm adForm,
            BindingResult bindingResult,
            @PathVariable Long id,
            Model model) {

        Ad ad = this.adService.getAdById(id);

        ad.setFavoris(!ad.isFavoris());

        try {
            this.adService.updateAd(id, ad);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to update ad: " + e.getMessage());
            return "ad-list";
        }

        return "redirect:/ads";
    }

}
