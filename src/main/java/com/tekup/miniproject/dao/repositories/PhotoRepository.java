package com.tekup.miniproject.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tekup.miniproject.dao.entities.Photo;

public interface PhotoRepository extends JpaRepository<Photo ,Long>{

}
