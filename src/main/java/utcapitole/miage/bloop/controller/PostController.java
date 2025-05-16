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
            if (utilisateur == null) return "login";

            Post post = new Post();
            post.setTextePost(postDTO.getTextePost());
            post.setUtilisateur(utilisateur);
            post.setDatePost(new Date());

            MultipartFile imageFile = postDTO.getImageFile();
            if (imageFile != null && !imageFile.isEmpty()) {
                if (!imageFile.getContentType().startsWith("image/")) {
                    model.addAttribute("error", "Le fichier doit être une image.");
                    model.addAttribute("post", postDTO);
                    return "creerPost";
                }
                post.setImagePost(imageFile.getBytes());
            }

            postService.creerPost(post);
            model.addAttribute("message", "Post créé avec succès !");
            return "confirmationPost";
        } catch (IOException e) {
            model.addAttribute("error", "Erreur lors du traitement de l'image.");
        } catch (Exception e) {
            model.addAttribute("error", "Une erreur est survenue lors de la création du post.");
        }
        model.addAttribute("post", postDTO);
        return "creerPost";
    }


    private Utilisateur getUtilisateurConnecte() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Utilisateur) authentication.getPrincipal();
    }
}