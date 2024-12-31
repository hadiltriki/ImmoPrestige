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
import org.springframework.security.core.Authentication;
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
@RequestMapping
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

    @RequestMapping({ "/", "/home" })
    public String getHomePage(Model model) {
        List<Ad> ads = this.adService.getAds(); // Récupérer toutes les annonces
        int adsCount = ads.size(); // Nombre d'annonces dans la liste

        // Condition pour limiter le nombre d'annonces affichées
        if (adsCount >= 3) {

            ads = ads.subList(ads.size() - 3, ads.size());

            // Afficher 3 dernieres annonces
        } else {
            ads = new ArrayList<>(); // Ne rien afficher si la taille est inférieure à 1 ou autre condition
        }

        model.addAttribute("ads", ads);

        return "homePrincipale"; // Retourner la vue avec la liste des annonces
    }

    @RequestMapping("/ads")
    public String getAllPersons(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "null") String sortByPrice,
            @RequestParam(defaultValue = "6") int pageSize, Model model) {
        Page<Ad> ads = this.adService.getAllAdPagination(PageRequest.of(page, pageSize));
        OptionalDouble minPrice = ads.stream().mapToDouble(Ad::getPrice).min();
        OptionalDouble maxPrice = ads.stream().mapToDouble(Ad::getPrice).max();
        OptionalDouble minArea = ads.stream().mapToDouble(Ad::getArea).min();
        OptionalDouble maxArea = ads.stream().mapToDouble(Ad::getArea).max();
        List<Integer> distinctRooms = ads.stream()
                .map(Ad::getNumberOfRooms) // Récupérer le nombre de chambres
                .distinct() // Garder les valeurs distinctes
                .sorted() // Trier les valeurs
                .limit(4) // Limiter à 4
                .collect(Collectors.toList()); // Collecter dans une liste

        // Récupérer la 4ème valeur (si elle existe)
        Integer roomsPlus = distinctRooms.size() == 4 ? distinctRooms.get(3) : null;

        // Ajouter la liste au modèle pour l'utiliser dans la vue
        model.addAttribute("distinctRooms", distinctRooms);
        model.addAttribute("roomsPlus", roomsPlus);
        // Le numéro de la page actuelle (getNumber()).
        model.addAttribute("ads", ads.getContent());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", ads.getTotalPages());
        model.addAttribute("minPrice", minPrice.isPresent() ? minPrice.getAsDouble() : 0);
        model.addAttribute("maxPrice", maxPrice.isPresent() ? maxPrice.getAsDouble() : 0);
        model.addAttribute("minArea", minArea.isPresent() ? minArea.getAsDouble() : 0);
        model.addAttribute("maxArea", maxArea.isPresent() ? maxArea.getAsDouble() : 0);
        model.addAttribute("sortByPrice", sortByPrice);
        model.addAttribute("categories", this.categoryService.getCategories());
        return "catalogue";
    }

    @RequestMapping("/ads/filter")
    public String getAdSorted(
            @RequestParam(required = false) String sortByPrice,
            @RequestParam(required = false) String sortBySurface,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int pageSize,
            Model model) {
        Page<Ad> adPage;

        // Gestion des triages par prix et par surface
        if (sortByPrice != null && !sortByPrice.isEmpty()) {
            adPage = this.adService.getAdSortedByPricePagination(
                    sortByPrice,
                    PageRequest.of(page, pageSize));
        } else if (sortBySurface != null && !sortBySurface.isEmpty()) {
            adPage = this.adService.getAdSortedBySurfacePagination(
                    sortBySurface,
                    PageRequest.of(page, pageSize));
        } else {
            adPage = this.adService.getAllAdPagination(PageRequest.of(page, pageSize));
        }
        Page<Ad> ads = this.adService.getAllAdPagination(PageRequest.of(page, pageSize));

        OptionalDouble minPrice = ads.stream().mapToDouble(Ad::getPrice).min();
        OptionalDouble maxPrice = ads.stream().mapToDouble(Ad::getPrice).max();
        OptionalDouble minArea = ads.stream().mapToDouble(Ad::getArea).min();
        OptionalDouble maxArea = ads.stream().mapToDouble(Ad::getArea).max();
        List<Integer> distinctRooms = ads.stream()
                .map(Ad::getNumberOfRooms) // Récupérer le nombre de chambres
                .distinct() // Garder les valeurs distinctes
                .sorted() // Trier les valeurs
                .limit(4) // Limiter à 4
                .collect(Collectors.toList()); // Collecter dans une liste

        // Récupérer la 4ème valeur (si elle existe)
        Integer roomsPlus = distinctRooms.size() == 4 ? distinctRooms.get(3) : null;

        // Ajouter la liste au modèle pour l'utiliser dans la vue
        model.addAttribute("distinctRooms", distinctRooms);
        model.addAttribute("roomsPlus", roomsPlus);

        model.addAttribute("ads", adPage.getContent());
        model.addAttribute("sortByPrice", sortByPrice);
        model.addAttribute("sortBySurface", sortBySurface);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", adPage.getTotalPages());
        model.addAttribute("minPrice", minPrice.isPresent() ? minPrice.getAsDouble() : 0);
        model.addAttribute("maxPrice", maxPrice.isPresent() ? maxPrice.getAsDouble() : 0);
        model.addAttribute("minArea", minArea.isPresent() ? minArea.getAsDouble() : 0);
        model.addAttribute("maxArea", maxArea.isPresent() ? maxArea.getAsDouble() : 0);
        model.addAttribute("categories", this.categoryService.getCategories());
        return "catalogue";
    }

    @RequestMapping("/ads/create")
    public String showAddAdForm(Model model, Authentication authentication) {
        if (!(authentication != null && authentication.isAuthenticated())) {
            return "redirect:/access-denied";// Utilisateur déjà connecté
        }
        model.addAttribute("adForm", new AdForm());
        model.addAttribute("categories", this.categoryService.getCategories());
        return "add-ad";
    }

    @RequestMapping(path = "/ads/create", method = RequestMethod.POST)
    public String addAd(@Valid @ModelAttribute AdForm adForm,
            BindingResult bindingResult,
            @RequestParam MultipartFile file,
            Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("adForm", adForm);
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
        System.out.println("Titre : " + adForm.getTitle());
        System.out.println("Catégorie : " + adForm.getCategoryId());
        System.out.println("Fichier : " + file.getOriginalFilename());

        return "redirect:/ads";
    }

    @RequestMapping("ads/{id}/edit")
    public String showEditAdForm(@PathVariable Long id, Model model, Authentication authentication) {
        if (!(authentication != null && authentication.isAuthenticated())) {
            return "redirect:/access-denied";
        }
        Ad ad = this.adService.getAdById(id);

        model.addAttribute("adForm", new AdForm(
                ad.getTitle(),
                ad.getDescription(),
                ad.getLocation(),
                ad.getContact(),
                ad.getPrice(),
                ad.getNumberOfRooms(),
                ad.getArea(),
                ad.getCategory().getId(),
                ad.getPhoto(),
                ad.getAdType()

        ));
        model.addAttribute("id", id);

        model.addAttribute("categories", this.categoryService.getCategories());

        return "edit-ad";
    }

    @RequestMapping(path = "ads/{id}/edit", method = RequestMethod.POST)
    public String editAd(
            @Valid @ModelAttribute AdForm adForm,
            BindingResult bindingResult,
            @PathVariable Long id,
            Model model,
            @RequestParam MultipartFile file) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("adForm", new AdForm());
            model.addAttribute("categories", this.categoryService.getCategories());

            model.addAttribute("errors", bindingResult.getAllErrors());
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

    @RequestMapping(path = "/ads/{id}/delete", method = RequestMethod.POST)
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

    @GetMapping("/ads/search")
    public String searchAds(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) Double area,
            @RequestParam(required = false) Integer rooms,

            @RequestParam(required = false) String category,
            @RequestParam(required = false) String adType,
            Model model) {
        List<Ad> results = new ArrayList<>();

        // Récupérer les résultats par adType, si spécifié
        if (adType != null && !"all".equalsIgnoreCase(adType) && !adType.isEmpty()) {
            results.addAll(adService.getAdByType(adType));
        }

        OptionalDouble minAreaOpt = adService.getAds().stream()
                .mapToDouble(Ad::getArea)
                .min();

        if (minAreaOpt.isPresent() && area != minAreaOpt.getAsDouble()) {
            results.addAll(adService.getAdByArea(area));
        }

        if (rooms != 0) {
            results.addAll(adService.getAdByRooms(rooms));
        }
        if (rooms == -1) {
            // Récupérer toutes les annonces
            List<Ad> ads = adService.getAds();

            // Récupérer les valeurs distinctes des chambres
            List<Integer> distinctRooms = ads.stream()
                    .map(Ad::getNumberOfRooms)
                    .distinct()
                    .sorted()
                    .skip(4)
                    .collect(Collectors.toList());

            // Filtrer les annonces pour correspondre aux chambres distinctes récupérées
            for (Integer roomCount : distinctRooms) {
                results.addAll(adService.getAdByRooms(roomCount));
            }
        }

        OptionalDouble minPriceOp = adService.getAds().stream().mapToDouble(Ad::getPrice).min();

        if (price != minPriceOp.getAsDouble()) {
            results.addAll(adService.getAdByPrice(price));
        }

        if (location != "" && !location.isEmpty()) {
            results.addAll(adService.getAdByLocation(location.toLowerCase()));
        }

        // Récupérer les résultats par category, si spécifié
        if (category != null && !"all".equalsIgnoreCase(category) && !category.isEmpty()) {
            results.addAll(adService.getAdByCategory(category));
        }
        if ("all".equalsIgnoreCase(category)
                && location == ""
                && price == minPriceOp.getAsDouble()
                && rooms == 0
                && "all".equalsIgnoreCase(adType)
                && area == minAreaOpt.getAsDouble()) {
            // Si toutes les conditions sont remplies, récupérer toutes les annonces
            return "redirect:/ads";
        }

        // Supprimer les doublons
        results = results.stream()
                .distinct()
                .collect(Collectors.toList());

        // Vérifier si la liste est vide
        if (results.isEmpty()) {
            results = null;
        }

        List<Ad> ads = this.adService.getAds();
        OptionalDouble minPrice = ads.stream().mapToDouble(Ad::getPrice).min();
        OptionalDouble maxPrice = ads.stream().mapToDouble(Ad::getPrice).max();
        OptionalDouble minArea = ads.stream().mapToDouble(Ad::getArea).min();
        OptionalDouble maxArea = ads.stream().mapToDouble(Ad::getArea).max();
        List<Integer> distinctRooms = ads.stream()
                .map(Ad::getNumberOfRooms) // Récupérer le nombre de chambres
                .distinct() // Garder les valeurs distinctes
                .sorted() // Trier les valeurs
                .limit(4) // Limiter à 4
                .collect(Collectors.toList()); // Collecter dans une liste

        // Récupérer la 4ème valeur (si elle existe)
        Integer roomsPlus = distinctRooms.size() == 4 ? distinctRooms.get(3) : null;

        // Ajouter la liste au modèle pour l'utiliser dans la vue
        model.addAttribute("distinctRooms", distinctRooms);
        model.addAttribute("roomsPlus", roomsPlus);

        model.addAttribute("minPrice", minPrice.isPresent() ? minPrice.getAsDouble() : 0);
        model.addAttribute("maxPrice", maxPrice.isPresent() ? maxPrice.getAsDouble() : 0);
        model.addAttribute("minArea", minArea.isPresent() ? minArea.getAsDouble() : 0);
        model.addAttribute("maxArea", maxArea.isPresent() ? maxArea.getAsDouble() : 0);

        model.addAttribute("categories", this.categoryService.getCategories());
        model.addAttribute("ads", results);
        return "catalogue";

    }

}
