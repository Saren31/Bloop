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

/**
 * Contrôleur pour gérer les opérations liées aux posts.
 * Fournit des endpoints pour créer des posts, afficher des posts et ajouter des commentaires.
 */
@Controller
@RequestMapping("/post")
public class PostController {

    private static final String CREER_POST_VIEW = "creerPost";
    private static final String ERROR_ATTRIBUTE = "error";

    private final PostService postService;
    private final CommentaireService commentaireService;

    /**
     * Constructeur pour injecter les services nécessaires.
     *
     * @param postService Service pour gérer les posts.
     * @param commentaireService Service pour gérer les commentaires.
     */
    @Autowired
    public PostController(PostService postService, CommentaireService commentaireService) {
        this.postService = postService;
        this.commentaireService = commentaireService;
    }

    /**
     * Affiche le formulaire pour créer un nouveau post.
     *
     * @param model Le modèle pour passer des données à la vue.
     * @return Le nom de la vue pour créer un post.
     */
    @GetMapping("/creer")
    public String afficherFormulaire(Model model) {
        model.addAttribute("post", new PostDTO());
        return CREER_POST_VIEW;
    }

    /**
     * Traite la création d'un nouveau post.
     *
     * @param postDTO Les données du post à créer.
     * @param model Le modèle pour passer des données à la vue.
     * @return Le nom de la vue de confirmation ou de création en cas d'erreur.
     */
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

    /**
     * Récupère l'utilisateur actuellement connecté.
     *
     * @return L'utilisateur connecté ou null s'il n'est pas authentifié.
     */
    private Utilisateur getUtilisateurConnecte() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Utilisateur) authentication.getPrincipal();
    }

    /**
     * Ajoute un commentaire à un post.
     *
     * @param postId L'identifiant du post à commenter.
     * @param texte Le texte du commentaire.
     * @param model Le modèle pour passer des données à la vue.
     * @return Une redirection vers la page du post.
     */
    @PostMapping("/{postId}/commenter")
    public String commenterPost(@PathVariable Long postId, @RequestParam String texte, Model model) {
        Utilisateur utilisateur = getUtilisateurConnecte();
        if (utilisateur == null) return "login";
        commentaireService.ajouterCommentaire(postId, texte, utilisateur);
        return "redirect:/post/" + postId;
    }

    /**
     * Affiche les détails d'un post, y compris ses commentaires.
     *
     * @param postId L'identifiant du post à afficher.
     * @param model Le modèle pour passer des données à la vue.
     * @return Le nom de la vue pour afficher le post.
     */
    @GetMapping("/{postId}")
    public String afficherPost(@PathVariable Long postId, Model model) {
        Post post = postService.getPostParId(postId);
        model.addAttribute("post", post);
        model.addAttribute("commentaires", commentaireService.getCommentairesParPost(postId));
        return "afficherPost";
    }

    /**
     * Supprime un post appartenant à l'utilisateur connecté.
     *
     * @param postId ID du post à supprimer
     * @param model  Le modèle pour afficher des messages
     * @return redirection vers le profil ou une page d'erreur
     */
    @GetMapping("/{postId}/confirmer-suppression")
    public String confirmerSuppression(@PathVariable Long postId, Model model) {
        Post post = postService.getPostParId(postId);
        if (post == null) {
            return "erreur";
        }
        model.addAttribute("post", post);
        return "confirmerSuppression";
    }


    @DeleteMapping("/{postId}/supprimer")
    public String supprimerPost(@PathVariable Long postId, Model model) {
        Utilisateur utilisateur = getUtilisateurConnecte();
        if (utilisateur == null) return "login";

        Post post;
        try {
            post = postService.getPostParId(postId);
        } catch (IllegalArgumentException e) {
            model.addAttribute(ERROR_ATTRIBUTE, "Post introuvable.");
            return "redirect:/profil/voirProfil";
        }

        Long postUserId = post.getUtilisateur().getIdUser();
        Long currentUserId = utilisateur.getIdUser();

        if (!postUserId.equals(currentUserId)) {
            model.addAttribute(ERROR_ATTRIBUTE, "Vous n'êtes pas autorisé à supprimer ce post.");
            return "redirect:/profil/voirProfil";
        }

        postService.supprimerPost(postId);
        return "redirect:/profil/voirProfil";
    }

    @GetMapping("/{postId}/modifier")
    public String afficherFormulaireModification(@PathVariable Long postId, Model model) {
        Utilisateur utilisateur = getUtilisateurConnecte();
        Post post = postService.getPostParId(postId);

        if (post == null || utilisateur == null || post.getUtilisateur().getIdUser() != utilisateur.getIdUser()) {
            return "redirect:/profil/voirProfil";
        }

        model.addAttribute("post", post);
        return "modifierPost";
    }

    @PostMapping("/{postId}/modifier")
    public String modifierPost(@PathVariable Long postId, @ModelAttribute Post postModifie, Model model) {
        Utilisateur utilisateur = getUtilisateurConnecte();
        Post post = postService.getPostParId(postId);

        if (post == null || utilisateur == null || post.getUtilisateur().getIdUser() != utilisateur.getIdUser()) {
            return "redirect:/profil/voirProfil";
        }

        post.setTextePost(postModifie.getTextePost());
        postService.save(post);

        return "redirect:/profil/voirProfil";
    }

}