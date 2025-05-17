package utcapitole.miage.bloop.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import utcapitole.miage.bloop.model.entity.Post;
import org.springframework.web.bind.annotation.*;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.PostService;
import utcapitole.miage.bloop.service.UtilisateurService;

import java.util.List;

/**
 * Contrôleur pour gérer les opérations liées au profil utilisateur.
 * Fournit des endpoints pour afficher, modifier et supprimer des profils, ainsi que pour voir les profils d'autres utilisateurs.
 */
@Controller
@RequestMapping("/profil")
public class ProfilController {

    private final UtilisateurService utilisateurService;
    private final PostService postService;

    /**
     * Constructeur pour injecter les services nécessaires.
     *
     * @param utilisateurService Service pour gérer les utilisateurs.
     * @param postService Service pour gérer les posts.
     */
    @Autowired
    public ProfilController(UtilisateurService utilisateurService, PostService postService) {
        this.utilisateurService = utilisateurService;
        this.postService = postService;
    }

    /**
     * Affiche le profil de l'utilisateur connecté.
     *
     * @param model Le modèle pour passer des données à la vue.
     * @return Le nom de la vue pour afficher le profil ou une redirection vers l'accueil si l'utilisateur n'est pas connecté.
     */
    @GetMapping("/voirProfil")
    public String afficherMonProfil(Model model) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurConnecte();

        if (utilisateur == null) {
            return "accueil"; // Redirige vers l'accueil si l'utilisateur n'est pas connecté
        }

        List<Post> posts = postService.getPostsByUtilisateur(utilisateur.getIdUser());

        model.addAttribute("utilisateur", utilisateur);
        model.addAttribute("posts", posts);

        return "voirProfil";
    }

    /**
     * Affiche le profil d'un autre utilisateur en fonction de son identifiant.
     *
     * @param id L'identifiant de l'utilisateur à afficher.
     * @param model Le modèle pour passer des données à la vue.
     * @param authentication L'authentification de l'utilisateur connecté.
     * @return Le nom de la vue pour afficher le profil ou une redirection si nécessaire.
     */
    @GetMapping("/voir/{id}")
    public String voirProfilAutre(@PathVariable Long id, Model model, Authentication authentication) {
        Utilisateur moi = (Utilisateur) authentication.getPrincipal();

        Utilisateur autre = utilisateurService.getUtilisateurParId(id);
        if (autre == null ) {
            return "accueil"; // Redirection vers l'accueil
        }
        if (autre.getIdUser() == moi.getIdUser() ) {
            return "redirect:/profil/voirProfil"; // Redirection vers son propre profil
        }

        model.addAttribute("utilisateur", autre);
        return "VoirAutreProfil";
    }

    /**
     * Supprime le profil de l'utilisateur connecté.
     *
     * @param request La requête HTTP pour gérer la session.
     * @return Une redirection vers la page de connexion avec un message approprié.
     */
    @DeleteMapping("/me")
    public String supprimerProfilConnecte(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof Utilisateur utilisateur) {
            utilisateurService.supprimerUtilisateurEtRelations(utilisateur.getIdUser());
            SecurityContextHolder.clearContext();
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            return "redirect:/auth/login?deleted";
        }
        return "redirect:/auth/login?error";
    }

    /**
     * Affiche le formulaire pour modifier le profil de l'utilisateur connecté.
     *
     * @param model Le modèle pour passer des données à la vue.
     * @return Le nom de la vue pour modifier le profil ou une redirection vers la page de connexion.
     */
    @GetMapping("/modifier")
    public String afficherFormulaireModification(Model model) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurConnecte();
        if (utilisateur == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("utilisateur", utilisateur);
        return "modifierProfil";
    }

    /**
     * Traite la modification du profil de l'utilisateur connecté.
     *
     * @param utilisateurModifie Les données modifiées du profil.
     * @return Une redirection vers la page du profil après modification ou vers la page de connexion si non authentifié.
     */
    @PostMapping("/modifier")
    public String modifierProfil(@ModelAttribute("utilisateur") Utilisateur utilisateurModifie) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurConnecte();
        if (utilisateur == null) {
            return "redirect:/auth/login";
        }
        utilisateur.setNomUser(utilisateurModifie.getNomUser());
        utilisateur.setPrenomUser(utilisateurModifie.getPrenomUser());
        utilisateur.setPseudoUser(utilisateurModifie.getPseudoUser());
        utilisateur.setTelUser(utilisateurModifie.getTelUser());
        utilisateur.setVisibiliteUser(utilisateurModifie.isVisibiliteUser());
        utilisateurService.save(utilisateur);
        return "redirect:/profil/voirProfil";
    }
}