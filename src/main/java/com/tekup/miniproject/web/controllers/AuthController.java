package com.tekup.miniproject.web.controllers;
/*package com.tekup.miniproject.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.tekup.miniproject.models.User;
import com.tekup.miniproject.models.UserForm;

import jakarta.validation.Valid;
@Controller
public class AuthController {
    User loggedInUser ;
     private static List<User> users = new ArrayList<User>(); 
private static Long idCount = 0L; 
static { 
users.add(new User(++idCount, "name1","name1","name1@gmail.com", "147258369", true,null,"21444555","Tunis")); 
users.add(new User(++idCount, "name2","name2","name2@gmail.com", "147258369", false,null,"21444555","Gabes")); 
users.add(new User(++idCount, "name3","name3","name3@gmail.com", "147258369", false,null,"21444555","Sfax")); 
users.add(new User(++idCount, "name4","name4","name4@gmail.com", "147258369", false,null,"21444555","Sousse")); 

}
      @GetMapping("/")
    public String showLoginForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "loginForm"; // Affiche la vue login.html
    }
       @PostMapping("/login")
    public String login(@Valid @ModelAttribute("userForm") UserForm userForm, 
                        BindingResult bindingResult, Model model) {
        
        // Si des erreurs de validation existent, renvoyer le formulaire avec les erreurs
        if (bindingResult.hasErrors()) {
            return "login";
        }

        // Vérification de l'existence de l'utilisateur et du mot de passe
        User authenticatedUser = authenticateUser(userForm.getEmail(), userForm.getPassword());

        if (authenticatedUser == null) {
            // Si l'utilisateur n'existe pas ou les informations sont incorrectes
            model.addAttribute("error", "Invalid email or password.");
            return "login"; // Renvoyer à la page de login avec l'erreur
        }

        // Si l'utilisateur est authentifié avec succès
        model.addAttribute("user", authenticatedUser);
        return "redirect:/home"; // Rediriger vers la page d'accueil après une connexion réussie
    }

    // Méthode pour authentifier l'utilisateur
    private User authenticateUser(String email, String password) {
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return user; // Retourner l'utilisateur authentifié
            }
        }
        return null; // Retourner null si l'utilisateur n'est pas trouvé ou les informations sont incorrectes
    }


    // Afficher la page d'enregistrement
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "register"; // Afficher la vue register.html
    }

    // Enregistrer un nouvel utilisateur
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userForm") UserForm userForm, 
                               BindingResult bindingResult, Model model) {

        // Vérification des erreurs de validation
        if (bindingResult.hasErrors()) {
            return "register"; // Renvoyer le formulaire si des erreurs de validation existent
        }

        // Vérification si l'email est déjà utilisé
        for (User user : users) {
            if (user.getEmail().equals(userForm.getEmail())) {
                model.addAttribute("error", "Email already exists.");
                return "register"; // Si l'email existe déjà, renvoyer le formulaire avec l'erreur
            }
        }

        // Créer un nouvel utilisateur et l'ajouter à la liste
        User newUser = new User(
                (long) (users.size() + 1),
                userForm.getFirstName(),
                userForm.getLastName(),
                userForm.getEmail(),
                userForm.getPassword(),
                false,
                "",  // Pas d'image pour l'instant
                userForm.getPhone(),
                userForm.getAddress()
        );
        users.add(newUser);

        // Connecter l'utilisateur après l'enregistrement
        loggedInUser = newUser;

        // Rediriger vers la page d'accueil
        return "redirect:/home";
    }

    // Afficher la page d'accueil avec les informations de l'utilisateur connecté
    @GetMapping("/home")
    public String showHomePage(Model model) {
        if (loggedInUser == null) {
            return "redirect:/login"; // Si l'utilisateur n'est pas connecté, rediriger vers la page de login
        }
        model.addAttribute("user", loggedInUser); // Passer les informations de l'utilisateur à la vue
        return "home"; // Afficher la vue home.html
    }

    // Méthode pour simuler la déconnexion
    @GetMapping("/logout")
    public String logout() {
        loggedInUser = null; // Réinitialiser l'utilisateur connecté
        return "redirect:/loginForm"; // Rediriger vers la page de login après la déconnexion
    }


}
*/