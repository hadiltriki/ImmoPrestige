package com.tekup.miniproject.dao.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tekup.miniproject.dao.entities.Category;
public interface CategoryRepository extends  JpaRepository<Category ,Long> {
  List<Category> findByName(String name);
}
