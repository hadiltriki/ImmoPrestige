package com.tekup.miniproject.business.servicesImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tekup.miniproject.business.services.AdService;
import com.tekup.miniproject.dao.entities.Ad;
import com.tekup.miniproject.dao.entities.Photo;
import com.tekup.miniproject.dao.repositories.AdRepository;
import com.tekup.miniproject.dao.repositories.PhotoRepository;

import jakarta.transaction.Transactional;
@Service
public class AdServiceImpl implements AdService{
    @Autowired 
    private final AdRepository adRepository;
    @Autowired
    private PhotoRepository photoRepository;
    public AdServiceImpl(AdRepository adRepository, PhotoRepository photoRepository){
     this.adRepository=adRepository;
     this.photoRepository=photoRepository;
     

    }
    @Override
    public List<Ad> getAds() {
       return  this.adRepository.findAll();
    }

    @Override
    public Ad getAdById(Long id) {
       return this.adRepository.findById(id).get();
    }

    @Override
    public List<Ad> getAdByLocation(String location) {
        return this.adRepository.findByLocation(location);
    }

    @Override
    public List<Ad> getAdByTitle(String title) {
        return this.adRepository.findByTitle(title);
    }

    @Override
    public List<Ad> getAdByPrice(Double price) {
        return this.adRepository.findByPrice(price);
    }

    @Override
    public List<Ad> getAdByCategoryName(String nameCategory) {
        return this.adRepository.findByCategoryName(nameCategory);
    }

    @Override
    public List<Ad> getAdByArea(Double area) {
        return this.adRepository.findByArea(area);
    }

    @Override
    public Ad addAd(Ad ad) {
        if(ad==null){
            return null;
        }
       return this.adRepository.save(ad);
    }

    @Override
    public Ad updateAd(Ad ad) {
        if(ad==null){

            return null;
        }
        List<Photo> currentPhotos = new ArrayList<>(ad.getPhotos());
    for (Photo photo : currentPhotos) {
        // Vous pouvez vérifier si la photo est orpheline et doit être supprimée
        photoRepository.delete(photo); // Supprimer la photo de la base de données
    }

    // Vider la collection des photos
    ad.getPhotos().clear();

       return this.adRepository.save(ad);
    }

    @Override
    public void deleteAdById(Long id) {
        if(id==null){
            return ;
        }
         this.adRepository.deleteById(id);
    
    }
    @Override
    public void addPhotos(List<Photo> photos) {
     
        // Enregistrer les photos dans la base de données
        for (Photo photo : photos) {
            photoRepository.save(photo);
        }
    
    }
    
}
