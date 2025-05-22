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
 * Fournit des endpoints pour créer, afficher, modifier, commenter et supprimer des posts.
 */
@Controller
@RequestMapping("/post")
public class PostController {

    private static final String CREER_POST_VIEW = "creerPost";
    private static final String LOGIN_VIEW = "login";
    private static final String ERROR_ATTRIBUTE = "error";
    private static final String REDIRECT_PROFIL_VOIR_PROFIL = "redirect:/profil/voirProfil";

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
            if (utilisateur == null) return LOGIN_VIEW;
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
        if (utilisateur == null) return LOGIN_VIEW;
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
     * Affiche une page de confirmation pour la suppression d'un post.
     *
     * @param postId L'identifiant du post à supprimer.
     * @param model Le modèle pour passer des données à la vue.
     * @return Le nom de la vue de confirmation.
     */
    @GetMapping("/{postId}/confirmer-suppression")
    public String confirmerSuppression(@PathVariable Long postId, Model model) {
        Post post = postService.getPostParId(postId);
        model.addAttribute("post", post);
        return "confirmerSuppression";
    }

    /**
     * Supprime un post appartenant à l'utilisateur connecté.
     *
     * @param postId L'identifiant du post à supprimer.
     * @param model Le modèle pour afficher des messages.
     * @return Une redirection vers le profil ou une page d'erreur.
     */
    @DeleteMapping("/{postId}/supprimer")
    public String supprimerPost(@PathVariable Long postId, Model model) {
        Utilisateur utilisateur = getUtilisateurConnecte();
        if (utilisateur == null) return LOGIN_VIEW;

        Post post;
        try {
            post = postService.getPostParId(postId);
        } catch (IllegalArgumentException e) {
            model.addAttribute(ERROR_ATTRIBUTE, "Post introuvable.");
            return REDIRECT_PROFIL_VOIR_PROFIL;
        }

        Long postUserId = post.getUtilisateur().getIdUser();
        Long currentUserId = utilisateur.getIdUser();

        if (!postUserId.equals(currentUserId)) {
            model.addAttribute(ERROR_ATTRIBUTE, "Vous n'êtes pas autorisé à supprimer ce post.");
            return REDIRECT_PROFIL_VOIR_PROFIL;
        }

        postService.supprimerPost(postId);
        return REDIRECT_PROFIL_VOIR_PROFIL;
    }

    /**
     * Affiche le formulaire pour modifier un post.
     *
     * @param postId L'identifiant du post à modifier.
     * @param model Le modèle pour passer des données à la vue.
     * @return Le nom de la vue pour modifier un post.
     */
    @GetMapping("/{postId}/modifier")
    public String afficherFormulaireModification(@PathVariable Long postId, Model model) {
        Utilisateur utilisateur = getUtilisateurConnecte();
        Post post = postService.getPostParId(postId);

        if (post == null || utilisateur == null || post.getUtilisateur().getIdUser() != utilisateur.getIdUser()) {
            return REDIRECT_PROFIL_VOIR_PROFIL;
        }

        model.addAttribute("post", post);
        return "modifierPost";
    }

    /**
     * Traite la modification d'un post.
     * Modifie le texte du post si l'utilisateur connecté est bien le propriétaire du post.
     * Redirige toujours vers la page de profil, que la modification ait eu lieu ou non.
     *
     * @param postId L'identifiant du post à modifier.
     * @param postModifie Les nouvelles données du post (seul le texte est pris en compte).
     * @return Une redirection vers la page de profil après la tentative de modification.
     */
    @PostMapping("/{postId}/modifier")
    public String modifierPost(@PathVariable Long postId, @ModelAttribute Post postModifie) {
        Utilisateur utilisateur = getUtilisateurConnecte();
        Post post = postService.getPostParId(postId);

        if (post != null && utilisateur != null && post.getUtilisateur().getIdUser() == utilisateur.getIdUser() ) {
            post.setTextePost(postModifie.getTextePost());
            postService.save(post);
        }

        return REDIRECT_PROFIL_VOIR_PROFIL;
    }
}