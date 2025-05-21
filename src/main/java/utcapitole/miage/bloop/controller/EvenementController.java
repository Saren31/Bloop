package utcapitole.miage.bloop.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import utcapitole.miage.bloop.model.entity.Evenement;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.EvenementService;
import utcapitole.miage.bloop.service.UtilisateurService;

import java.util.List;

/**
 * Contrôleur pour gérer les opérations liées aux événements.
 */
@Controller
@RequestMapping("/evenement")
public class EvenementController {

    private final EvenementService evenementService;
    private final UtilisateurService utilisateurService;

    /**
     * Constructeur pour injecter les services nécessaires.
     *
     * @param evenementService Service pour la gestion des événements.
     * @param utilisateurService Service pour la gestion des utilisateurs.
     */
    @Autowired
    public EvenementController(EvenementService evenementService, UtilisateurService utilisateurService) {
        this.evenementService = evenementService;
        this.utilisateurService = utilisateurService;
    }

    /**
     * Affiche le formulaire de création d'un événement.
     *
     * @param model Modèle pour passer des données à la vue.
     * @return Le nom de la vue "creerEvenement".
     */
    @GetMapping("/creer")
    public String afficherFormulaire(Model model) {
        model.addAttribute("evenement", new Evenement());
        return "creerEvenement";
    }

    /**
     * Crée un nouvel événement.
     *
     * @param evenement L'objet événement à créer.
     * @param session La session HTTP en cours.
     * @param model Modèle pour passer des données à la vue.
     * @return Une redirection vers la page de profil ou la page de connexion si l'utilisateur n'est pas connecté.
     */
    @PostMapping("/creer")
    public String creerEvenement(@ModelAttribute Evenement evenement, HttpSession session, Model model) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurConnecte();

        if (utilisateur == null) {
            return "login";
        }

        evenement.setOrganisateur(utilisateur);
        evenementService.creerEvenement(evenement);
        model.addAttribute("message", "Événement créé avec succès !");
        return "redirect:/profil/voirProfil";
    }

    /**
     * Affiche les événements créés par l'utilisateur connecté.
     *
     * @param model Modèle pour passer des données à la vue.
     * @return Le nom de la vue "mesEvenements" ou "login" si l'utilisateur n'est pas connecté.
     */
    @GetMapping("/mesEvenements")
    public String afficherMesEvenements(Model model) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurConnecte();
        if (utilisateur == null) return "login";

        List<Evenement> evenements = evenementService.getEvenementsParOrganisateur(utilisateur.getIdUser());
        model.addAttribute("evenements", evenements);
        return "mesEvenements";
    }

    /**
     * Affiche les détails d'un événement spécifique.
     *
     * @param id L'identifiant de l'événement.
     * @param model Modèle pour passer des données à la vue.
     * @return Le nom de la vue "detailEvenement" ou une redirection si l'événement n'existe pas.
     */
    @GetMapping("/{id}")
    public String afficherDetailEvenement(@PathVariable Long id, Model model) {
        Evenement evenement = evenementService.getEvenementParId(id);
        Utilisateur utilisateur = utilisateurService.getUtilisateurConnecte();

        if (evenement == null) {
            return "redirect:/profil/voirProfil";
        }

        model.addAttribute("evenement", evenement);
        model.addAttribute("inscrit", utilisateur != null && evenementService.estInscrit(evenement, utilisateur));
        model.addAttribute("interesse", utilisateur != null && evenementService.estInteresse(evenement, utilisateur));
        return "detailEvenement";
    }

    /**
     * Inscrit l'utilisateur connecté à un événement.
     *
     * @param id L'identifiant de l'événement.
     * @return Une redirection vers la page de détails de l'événement.
     */
    @PostMapping("/inscription/{id}")
    public String inscrire(@PathVariable Long id) {
        Evenement evenement = evenementService.getEvenementParId(id);
        Utilisateur utilisateur = utilisateurService.getUtilisateurConnecte();

        if (evenement != null && utilisateur != null) {
            evenementService.inscrireUtilisateur(evenement, utilisateur);
        }
        return "redirect:/evenement/" + id;
    }

    /**
     * Désinscrit l'utilisateur connecté d'un événement.
     *
     * @param id L'identifiant de l'événement.
     * @return Une redirection vers la page de détails de l'événement.
     */
    @PostMapping("/desinscription/{id}")
    public String desinscrire(@PathVariable Long id) {
        Evenement evenement = evenementService.getEvenementParId(id);
        Utilisateur utilisateur = utilisateurService.getUtilisateurConnecte();

        if (evenement != null && utilisateur != null) {
            evenementService.retirerUtilisateur(evenement, utilisateur);
        }
        return "redirect:/evenement/" + id;
    }

    /**
     * Marque l'utilisateur connecté comme intéressé par un événement.
     *
     * @param id L'identifiant de l'événement.
     * @return Une redirection vers la page de détails de l'événement.
     */
    @PostMapping("/interet/{id}")
    public String marquerInteresse(@PathVariable Long id) {
        Evenement evenement = evenementService.getEvenementParId(id);
        Utilisateur utilisateur = utilisateurService.getUtilisateurConnecte();

        if (evenement != null && utilisateur != null) {
            evenementService.marquerInteresse(evenement, utilisateur);
        }
        return "redirect:/evenement/" + id;
    }

    /**
     * Retire l'intérêt de l'utilisateur connecté pour un événement.
     *
     * @param id L'identifiant de l'événement.
     * @return Une redirection vers la page de détails de l'événement.
     */
    @PostMapping("/retirerInteret/{id}")
    public String retirerInteresse(@PathVariable Long id) {
        Evenement evenement = evenementService.getEvenementParId(id);
        Utilisateur utilisateur = utilisateurService.getUtilisateurConnecte();

        if (evenement != null && utilisateur != null) {
            evenementService.retirerInteresse(evenement, utilisateur);
        }
        return "redirect:/evenement/" + id;
    }

    @GetMapping("/mesInscriptions")
    public String afficherMesInscriptions(Model model) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurConnecte();
        if (utilisateur == null) return "login";
        List<Evenement> evenements = evenementService.getEvenementsOuUtilisateurEstInscrit(utilisateur.getIdUser());
        model.addAttribute("evenements", evenements);
        return "mesInscriptions";
    }



    @GetMapping("/{id}/participants")
    public String afficherParticipants(@PathVariable Long id, Model model) {
        Evenement evenement = evenementService.getEvenementParId(id);
        Utilisateur utilisateur = utilisateurService.getUtilisateurConnecte();

        if (evenement.getOrganisateur().getIdUser() != utilisateur.getIdUser()) {
            return "redirect:/accueil";
        }

        model.addAttribute("evenement", evenement);
        model.addAttribute("participants", evenement.getInscrits());
        return "listeParticipants";
    }

}