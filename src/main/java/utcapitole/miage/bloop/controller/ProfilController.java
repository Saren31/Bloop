package utcapitole.miage.bloop.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import utcapitole.miage.bloop.dto.UtilisateurDTO;
import utcapitole.miage.bloop.model.entity.Evenement;
import utcapitole.miage.bloop.model.entity.Post;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/profil")
public class ProfilController {

    private final UtilisateurService utilisateurService;
    private final PostService postService;
    private final ReactionService reactionService;
    private final EvenementService evenementService;
    private final RelationService relationService;

    public static final String REDIRECT_VOIR_PROFIL = "redirect:/profil/voirProfil";
    public static final String ATTR_UTILISATEUR     = "utilisateur";

    @Autowired
    public ProfilController(UtilisateurService utilisateurService,
                            PostService postService,
                            ReactionService reactionService,
                            EvenementService evenementService,
                            RelationService relationService) {
        this.utilisateurService  = utilisateurService;
        this.postService         = postService;
        this.reactionService     = reactionService;
        this.evenementService    = evenementService;
        this.relationService     = relationService;
    }

    /**
     * Affiche le profil de l'utilisateur connecté (posts, événements, réactions, amis).
     */
    @Transactional(readOnly = true)
    @GetMapping("/voirProfil")
    public String afficherMonProfil(Model model) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurConnecte();
        if (utilisateur == null) {
            return "accueil";
        }

        // --- Posts et réactions ---
        List<Post> posts = postService.getPostsByUtilisateur(utilisateur.getIdUser());
        for (Post p : posts) {
            p.setLikedByCurrentUser(
                    reactionService.isLikedBy(p, utilisateur)
            );
            p.setDislikedByCurrentUser(
                    reactionService.isDislikedBy(p, utilisateur)
            );
            p.setLikeCount(reactionService.countLikes(p));
            p.setDislikeCount(reactionService.countDislikes(p));
        }

        // --- Événements ---
        List<Evenement> evts = utilisateurService.getEvenementsParUtilisateur(utilisateur);
        if (evts == null) evts = Collections.emptyList();
        evts = evts.stream().filter(Objects::nonNull).collect(Collectors.toList());

        Map<Long, Boolean> inscritMap   = new HashMap<>();
        Map<Long, Boolean> interesseMap = new HashMap<>();
        for (Evenement e : evts) {
            inscritMap.put(e.getId(), evenementService.estInscrit(e, utilisateur));
            interesseMap.put(e.getId(), evenementService.estInteresse(e, utilisateur));
        }

        // --- Amis sous forme de DTO ---
        List<UtilisateurDTO> amisDto = relationService.getListeAmis(utilisateur.getIdUser());

        model.addAttribute(ATTR_UTILISATEUR, utilisateur);
        model.addAttribute("posts",        posts);
        model.addAttribute("evenements",   evts);
        model.addAttribute("inscritMap",   inscritMap);
        model.addAttribute("interesseMap", interesseMap);
        model.addAttribute("amis",         amisDto);

        return "voirProfil";
    }

    /**
     * Affiche le profil d'un autre utilisateur, avec état de la relation.
     */
    @Transactional(readOnly = true)
    @GetMapping("/voir/{id}")
    public String voirProfilAutre(@PathVariable Long id,
                                  Model model,
                                  Authentication authentication) {

        // 1) Récupère le principal (non-géré)
        Object princ = authentication.getPrincipal();
        if (!(princ instanceof Utilisateur)) {
            return REDIRECT_VOIR_PROFIL;
        }
        Long moiId = ((Utilisateur) princ).getIdUser();

        // 2) Recharge "moi" pour avoir une entité gérée
        Utilisateur moi = utilisateurService.getUtilisateurById(moiId);
        if (moi == null) {
            return "redirect:/auth/login";
        }

        // 3) Récupère l'autre utilisateur
        Utilisateur autre = utilisateurService.getUtilisateurById(id);
        if (autre == null) {
            return "redirect:/accueil";
        }
        if (Objects.equals(autre.getIdUser(), moi.getIdUser())) {
            return REDIRECT_VOIR_PROFIL;
        }

        // 4) Statut d'amitié
        boolean estAmi         = moi.getAmis().contains(autre);
        boolean demandeRecue   = moi.getDemandesRecues().contains(autre);
        boolean demandeEnvoyee = moi.getDemandesEnvoyees().contains(autre);

        List<Post> postsAutre = postService.getPostsByUtilisateur(autre.getIdUser());

        model.addAttribute(ATTR_UTILISATEUR, autre);
        model.addAttribute("estAmi",         estAmi);
        model.addAttribute("demandeRecue",   demandeRecue);
        model.addAttribute("demandeEnvoyee", demandeEnvoyee);
        model.addAttribute("postsAutre",     postsAutre);

        return "voirAutreProfil";
    }

    /**
     * Supprime le profil de l'utilisateur connecté et invalide la session.
     */
    @DeleteMapping("/me")
    public String supprimerProfilConnecte(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object princ = auth.getPrincipal();
        if (princ instanceof Utilisateur u) {
            utilisateurService.supprimerUtilisateurEtRelations(u.getIdUser());
            SecurityContextHolder.clearContext();
            HttpSession session = request.getSession(false);
            if (session != null) session.invalidate();
            return "redirect:/auth/login?deleted";
        }
        return "redirect:/auth/login?error";
    }

    /**
     * Formulaire de modification du profil.
     */
    @GetMapping("/modifier")
    public String afficherFormulaireModification(Model model) {
        Utilisateur u = utilisateurService.getUtilisateurConnecte();
        if (u == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("utilisateur", u);
        return "modifierProfil";
    }

    /**
     * Traite la modification du profil.
     */
    @PostMapping("/modifier")
    public String modifierProfil(@ModelAttribute("utilisateur") Utilisateur modif) {
        Utilisateur u = utilisateurService.getUtilisateurConnecte();
        if (u == null) {
            return "redirect:/auth/login";
        }
        u.setNomUser(modif.getNomUser());
        u.setPrenomUser(modif.getPrenomUser());
        u.setPseudoUser(modif.getPseudoUser());
        u.setTelUser(modif.getTelUser());
        u.setVisibiliteUser(modif.isVisibiliteUser());
        utilisateurService.save(u);
        return REDIRECT_VOIR_PROFIL;
    }

    /**
     * Redirection par défaut vers /voirProfil.
     */
    @GetMapping("")
    public String redirectionProfilParDefaut() {
        return REDIRECT_VOIR_PROFIL;
    }
}
