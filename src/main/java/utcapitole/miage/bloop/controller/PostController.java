package utcapitole.miage.bloop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import utcapitole.miage.bloop.dto.PostDTO;
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.PostService;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/creer")
    public String afficherFormulaire(Model model) {
        model.addAttribute("post", new PostDTO());
        return "creerPost";
    }

    @PostMapping("/creer")
    public String creerPost(@ModelAttribute PostDTO postDTO, Model model) {
        try {
            Utilisateur utilisateur = getUtilisateurConnecte();
            if (utilisateur == null) {
                return "login"; // Rediriger vers login si pas connecté
            }

            Post post = new Post();
            post.setTextePost(postDTO.getTextePost());
            post.setUtilisateur(utilisateur);
            post.setDatePost(new Date());

            // Déléguer le traitement de l'image à un service
            if (postDTO.getImageFile() != null && !postDTO.getImageFile().isEmpty()) {
                if (!postDTO.getImageFile().getContentType().startsWith("image/")) {
                    model.addAttribute("error", "Le fichier doit être une image.");
                    return "creerPost";
                }
                post.setImagePost(postDTO.getImageFile().getBytes());
            }

            postService.creerPost(post);
            model.addAttribute("message", "Post créé avec succès !");
            return "confirmationPost";
        } catch (IOException e) {
            model.addAttribute("error", "Erreur lors du traitement de l'image.");
            return "creerPost";
        } catch (Exception e) {
            model.addAttribute("error", "Une erreur est survenue lors de la création du post.");
            return "creerPost";
        }
    }

    private Utilisateur getUtilisateurConnecte() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Utilisateur) authentication.getPrincipal();
    }
}