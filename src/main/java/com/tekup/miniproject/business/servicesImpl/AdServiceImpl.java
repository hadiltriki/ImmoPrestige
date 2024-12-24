package com.tekup.miniproject.business.servicesImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.tekup.miniproject.business.services.AdService;
import com.tekup.miniproject.dao.entities.Ad;

import com.tekup.miniproject.dao.repositories.AdRepository;


import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class AdServiceImpl implements AdService{
    @Autowired 
    private final AdRepository adRepository;

   
 
    public AdServiceImpl(AdRepository adRepository){
     this.adRepository=adRepository;
    }

    @Override
    public List<Ad> getAds() {
       return  this.adRepository.findAll();
    }

    @Override
    public Ad getAdById(Long id) {
        if(id == null)
      {
        return null;
      }
      return this.adRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Ad with id : "+ id + " not found!"));
   
    }


    @Override
    public List<Ad> getAdByTitle(String title) {
        return this.adRepository.findByTitle(title);
    }

    @Override
    public List<Ad> getAdByType(String adType) {
        if (adType == null || adType.isEmpty()) {
            return null; 
        }
        return this.adRepository.findByAdType(adType);
    }

    @Override
    public Ad addAd(Ad ad) {
        if(ad==null){
            return null;
        }
       Ad newAd= new Ad();
       try{
        newAd= adRepository.save(ad);

       } catch(DataIntegrityViolationException e)
       {
        log.error(e.getMessage());
       }
       return newAd;
    }

    @Override
    public Ad updateAd(Long id, Ad ad) {
       Ad adExisting= this.getAdById(id);
       adExisting.setArea(ad.getArea());
       ad.setCategory(ad.getCategory());
       ad.setContact(ad.getContact());
    
       ad.setNumberOfRooms(ad.getNumberOfRooms());
       ad.setPhoto(ad.getPhoto());
       ad.setTitle(ad.getTitle());
       ad.setPrice(ad.getPrice());
       adExisting.setDescription(ad.getDescription());
       return adRepository.save(adExisting);
    }

    @Override
    public void deleteAdById(Long id) {
        if(id==null){
            return ;
        }
        else if(this.adRepository.existsById(id))
        {
            this.adRepository.deleteById(id);
        }
        else{
            throw new EntityNotFoundException("Ad with id: "+id+ " not found");
        }
    
    }
    @Override
    public List<Ad> getAdSortedByTitle(String order) {
        Sort.Direction direction= Sort.Direction.ASC;
        if("desc".equalsIgnoreCase(order)){
            direction= Sort.Direction.DESC;
        }

       return adRepository.findAll(Sort.by(direction,"title"));
      }
    @Override
    public List<Ad> getAdSortedByLocation(String order) {
        Sort.Direction direction= Sort.Direction.ASC;
        if("desc".equalsIgnoreCase(order)){
            direction= Sort.Direction.DESC;
        }

       return adRepository.findAll(Sort.by(direction,"location"));
       }
    @Override
    public List<Ad> getAdSortedByCategoryName(String order) {
        Sort.Direction direction= Sort.Direction.ASC;
        if("desc".equalsIgnoreCase(order)){
            direction= Sort.Direction.DESC;
        }

       return adRepository.findAll(Sort.by(direction,"name"));
       }
    @Override
    public List<Ad> getAdSortedByArea(String order) {
        Sort.Direction direction= Sort.Direction.ASC;
        if("desc".equalsIgnoreCase(order)){
            direction= Sort.Direction.DESC;
        }

       return adRepository.findAll(Sort.by(direction,"area"));
      
    }
    @Override
    public Page<Ad> getAllAdPagination(Pageable pegeable) {
        if(pegeable ==null){
            return null;
        }
        return this.adRepository.findAll(pegeable); }
    @Override
    public Page<Ad> getAdSortedByPricePagination(String order, Pageable pegeable) {
        if(pegeable ==null){
            return null;
        }  
        Sort.Direction direction= Sort.Direction.ASC;
        if("desc".equalsIgnoreCase(order)){
            direction= Sort.Direction.DESC;
        }
        Pageable sortedPageable=PageRequest.of(
            pegeable.getPageNumber(),
            pegeable.getPageSize(),
            Sort.by(direction,"price")
        );
        return this.adRepository.findAll(sortedPageable);
    }
   
    @Override
    public List<Ad> getAdByCategory(String category) {
        
        return this.adRepository.findByCategoryName(category);
         
       }
    @Override
    public List<Ad> getAdByPrice(Double price) {
       return this.adRepository.findByPrice(price);
    }
    @Override
    public List<Ad> getAdByLocation(String location) {
        return this.adRepository.findByLocation(location);
    }
  
    @Override
    public List<Ad> getAdByRooms(Integer rooms) {
        return this.adRepository.findByNumberOfRooms(rooms);
    }
    public List<Ad> searchAds(String title, String location, Double price, Integer rooms, String contact, String category) {
    // Liste pour stocker les résultats filtrés
    List<Ad> results;

    // Commencer par chercher tous les résultats
    results = adRepository.findAll();

    // Appliquer les filtres un par un
    if (title != null && !title.isEmpty()) {
        results = results.stream()
                         .filter(ad -> ad.getTitle().toLowerCase().contains(title.toLowerCase()))
                         .collect(Collectors.toList());
    }

    if (location != null && !location.isEmpty()) {
        results = results.stream()
                         .filter(ad -> ad.getLocation().toLowerCase().contains(location.toLowerCase()))
                         .collect(Collectors.toList());
    }

    if (price != null) {
        results = results.stream()
                         .filter(ad -> ad.getPrice() != null && ad.getPrice().compareTo(price) <= 0) // Par exemple, filtrer les annonces avec un prix inférieur ou égal à celui spécifié
                         .collect(Collectors.toList());
    }

    if (rooms != null) {
        results = results.stream()
                         .filter(ad -> ad.getNumberOfRooms() != null && ad.getNumberOfRooms().equals(rooms))
                         .collect(Collectors.toList());
    }

    if (contact != null && !contact.isEmpty()) {
        results = results.stream()
                         .filter(ad -> ad.getContact() != null && ad.getContact().toLowerCase().contains(contact.toLowerCase()))
                         .collect(Collectors.toList());
    }

    if (category != null && !category.isEmpty()) {
        results = results.stream()
                         .filter(ad -> ad.getCategory() != null && ad.getCategory().getName().toLowerCase().contains(category.toLowerCase()))
                         .collect(Collectors.toList());
    }

    return results;
}

    @Override
    public List<Ad> getAdByArea(Double area) {
        return this.adRepository.findByArea(area);
    }


    
}
