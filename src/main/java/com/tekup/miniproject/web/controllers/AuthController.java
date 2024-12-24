package com.tekup.miniproject.web.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.tekup.miniproject.business.services.UserService;
import com.tekup.miniproject.dao.entities.User;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/access-denied"; // Utilisateur déjà connecté
        }
        return "login"; // Affiche la page de login
    }

    @GetMapping("/access-denied")
    public String getAccessDeniedPage(Model model) {
        model.addAttribute("error", "You are not allowed to access this page");
        return "errors";
    }

    @GetMapping("/register")
    public String register(Authentication authentication, Model model) {
        if (authentication == null && !authentication.isAuthenticated()) {
            return "redirect:/access-denied";// Utilisateur déjà connecté
        }
        model.addAttribute("user", new User()); // Crée un nouvel utilisateur pour le formulaire
        return "register-user";
    }

    @PostMapping("/register")
    public String saveUser(@ModelAttribute User user, Model model) {
        try {
            List<String> roles = new ArrayList<>();
        roles.add("ADMIN");
            user.setRoles(roles);
            userService.saveUser(user); // Sauvegarde l'utilisateur
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("msgError", "Email or username already exists");
            return "register-user"; // Réaffiche le formulaire avec le message d'erreur
        }
        return "redirect:/home";
    }
}
