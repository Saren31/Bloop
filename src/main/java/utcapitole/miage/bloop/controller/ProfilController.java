package utcapitole.miage.bloop.controller;

import jakarta.servlet.http.HttpServletRequest;
import jdk.jshell.execution.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.EmailService;
import utcapitole.miage.bloop.service.UtilisateurService;

import java.util.UUID;

@Controller
@RequestMapping("/profil")
public class ProfilController {

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new Utilisateur());
        return "inscription";
    }

    @PostMapping("/register_user")
    public String registerUser(@ModelAttribute Utilisateur user, HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) {
        return utilisateurService.inscrireNouvelUtilisateur(user, request, model);
    }
}

