package com.tekup.miniproject;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tekup.miniproject.business.services.UserService;
import com.tekup.miniproject.dao.entities.User;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class MiniprojectApplication {
	/*@Autowired
	 UserService userService;*/

	public static void main(String[] args) {
		SpringApplication.run(MiniprojectApplication.class, args);
	}
 	/*@PostConstruct
    public void init() {
        List<String> roles = new ArrayList<>();
        roles.add("ADMIN");
		roles.add("SUPER_ADMIN");
        userService.saveUser(new User(null, "admin", "admin", "admin@gmail.com", roles));
    }   */

}
