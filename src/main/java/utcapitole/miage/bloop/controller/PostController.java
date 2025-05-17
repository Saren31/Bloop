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
import utcapitole.miage.bloop.service.CommentaireService;


import java.io.IOException;
import java.util.Date;

@Controller
@RequestMapping("/post")
public class PostController {

    private static final String CREER_POST_VIEW = "creerPost";
    private static final String ERROR_ATTRIBUTE = "error";

    private final PostService postService;

    private final CommentaireService commentaireService;

    @Autowired
    public PostController(PostService postService, CommentaireService commentaireService) {
        this.postService = postService;
        this.commentaireService = commentaireService;
    }

    @GetMapping("/creer")
    public String afficherFormulaire(Model model) {
        model.addAttribute("post", new PostDTO());
        return CREER_POST_VIEW;
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
                String contentType = imageFile.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    model.addAttribute(ERROR_ATTRIBUTE, "Le fichier doit être une image.");
                    model.addAttribute("post", postDTO);
                    return CREER_POST_VIEW;
                }
                post.setImagePost(imageFile.getBytes());
            }

            postService.creerPost(post);
            model.addAttribute("message", "Post créé avec succès !");
            return "confirmationPost";
        } catch (IOException e) {
            model.addAttribute(ERROR_ATTRIBUTE, "Erreur lors du traitement de l'image.");
        } catch (Exception e) {
            model.addAttribute(ERROR_ATTRIBUTE, "Une erreur est survenue lors de la création du post.");
        }
        model.addAttribute("post", postDTO);
        return CREER_POST_VIEW;
    }

    //a bouger dans un service
    private Utilisateur getUtilisateurConnecte() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Utilisateur) authentication.getPrincipal();
    }

    @PostMapping("/{postId}/commenter")
    public String commenterPost(@PathVariable Long postId, @RequestParam String texte, Model model) {
        Utilisateur utilisateur = getUtilisateurConnecte();
        if (utilisateur == null) return "login";
        commentaireService.ajouterCommentaire(postId, texte, utilisateur);
        return "redirect:/post/" + postId;
    }

    @GetMapping("/{postId}")
    public String afficherPost(@PathVariable Long postId, Model model) {
        Post post = postService.getPostParId(postId);
        model.addAttribute("post", post);
        model.addAttribute("commentaires", commentaireService.getCommentairesParPost(postId));
        return "afficherPost";
    }

}