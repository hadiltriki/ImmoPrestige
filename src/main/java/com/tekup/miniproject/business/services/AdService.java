package com.tekup.miniproject.business.services;

import java.util.List;

import com.tekup.miniproject.dao.entities.Ad;
import com.tekup.miniproject.dao.entities.Photo;

import jakarta.transaction.Transactional;



public interface AdService {
    List<Ad> getAds();
    Ad getAdById(Long id);
    List<Ad> getAdByLocation(String location);
    List<Ad> getAdByTitle(String title);
    List<Ad> getAdByPrice(Double price);
    List<Ad> getAdByCategoryName(String nameCategory);
    List<Ad> getAdByArea(Double area);
    //create
    Ad addAd(Ad ad);
    //Update
    Ad updateAd(Ad ad);
    //Delete
    void deleteAdById(Long id);
    
    public void addPhotos(List<Photo> photos); 
    
}
