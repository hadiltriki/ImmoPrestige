package com.tekup.miniproject.dao.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tekup.miniproject.dao.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByEmail(String email);

}
