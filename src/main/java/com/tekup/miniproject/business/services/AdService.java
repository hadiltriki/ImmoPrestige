package com.tekup.miniproject.business.services;

import java.util.List;
import java.util.Locale.Category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tekup.miniproject.dao.entities.Ad;





public interface AdService {
    List<Ad> getAds();
    Ad getAdById(Long id);

    List<Ad> getAdByTitle(String title);
    List<Ad> getAdByRooms(Integer number);
    List<Ad> getAdByCategory(String category);
    List<Ad> getAdByPrice(Double price);
    List<Ad> getAdByLocation(String location);
    List<Ad> getAdByArea(Double area);
    List<Ad> getAdByType(String adType);
   
 


    List<Ad> getAdSortedByTitle(String order);
    List<Ad> getAdSortedByLocation(String order);
    List<Ad> getAdSortedByCategoryName(String order);
    List<Ad> getAdSortedByArea(String order);
    
    Page<Ad> getAllAdPagination(Pageable pegeable);
    Page<Ad> getAdSortedByPricePagination(String order,Pageable pageable);
  
    //create
    Ad addAd(Ad ad);
    //Update
    Ad updateAd(Long id,Ad ad);
    //Delete
    void deleteAdById(Long id);
   
    
}
