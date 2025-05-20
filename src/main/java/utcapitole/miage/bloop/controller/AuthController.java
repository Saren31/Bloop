package utcapitole.miage.bloop.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.AuthService;

import java.io.IOException;

/**
 * Contrôleur pour gérer les requêtes liées à l'authentification.
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Constructeur pour injecter le service d'authentification.
     *
     * @param authService Service d'authentification utilisé pour gérer les utilisateurs.
     */
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Gère les requêtes GET pour afficher la page de connexion.
     *
     * @return Le nom de la vue "login" à afficher.
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    /**
     * Gère les requêtes GET pour afficher la page d'inscription.
     *
     * @param model Le modèle utilisé pour ajouter des attributs à la vue.
     * @return Le nom de la vue "inscription" à afficher.
     */
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new Utilisateur());
        return "inscription";
    }

    /**
     * Gère les requêtes POST pour enregistrer un nouvel utilisateur.
     *
     * @param user    L'objet Utilisateur contenant les informations de l'utilisateur à enregistrer.
     * @param request L'objet HttpServletRequest pour accéder aux informations de la requête.
     * @param model   Le modèle utilisé pour ajouter des attributs à la vue.
     * @return Une chaîne représentant la vue ou la redirection après l'enregistrement.
     */
    @PostMapping("/register_user")
    public String registerUser(@ModelAttribute Utilisateur user,
                               @RequestParam("avatarFile") MultipartFile avatarFile,
                               HttpServletRequest request,
                               Model model) {
        try {
            // Si une image a été uploadée, la convertir en bytes
            if (avatarFile != null && !avatarFile.isEmpty()) {
                user.setAvatarUser(avatarFile.getBytes());
            }

            // Appeler le service d'authentification avec l'utilisateur mis à jour
            return authService.enregistrerUtilisateur(user, request, model);

        } catch (IOException e) {
            model.addAttribute("error", "Erreur lors du téléchargement de l'image");
            return "inscription";
        }
    }





}