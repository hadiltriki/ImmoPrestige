package com.tekup.miniproject.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tekup.miniproject.dao.entities.Ad;


import java.util.List;



public interface AdRepository extends JpaRepository<Ad,Long> {
    List<Ad> findByCategoryName(String nameCategory);
    List<Ad> findByLocation(String location);
    List<Ad> findByPrice(Double price);
    List<Ad> findByTitle(String title);
    List<Ad> findByArea(Double area);
    List<Ad> findByNumberOfRooms(Integer numberOfRooms);
    List<Ad> findByContact(String contact);



    

    
}
