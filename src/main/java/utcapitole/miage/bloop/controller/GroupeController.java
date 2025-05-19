package utcapitole.miage.bloop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import utcapitole.miage.bloop.dto.GroupeDTO;
import utcapitole.miage.bloop.model.entity.Groupe;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.GroupeService;
import utcapitole.miage.bloop.service.UtilisateurService;

import java.io.IOException;

/**
 * Contrôleur pour gérer les opérations liées aux groupes.
 */
@Controller
@RequestMapping("/groupes")
public class GroupeController {

    private final GroupeService groupeService;
    private final UtilisateurService utilisateurService;

    /**
     * Constructeur pour injecter les services nécessaires.
     *
     * @param groupeService Service pour gérer les groupes.
     * @param utilisateurService Service pour gérer les utilisateurs.
     */
    @Autowired
    public GroupeController(GroupeService groupeService, UtilisateurService utilisateurService) {
        this.groupeService = groupeService;
        this.utilisateurService = utilisateurService;
    }

    /**
     * Affiche la page de création d'un groupe.
     *
     * @return Le nom de la vue pour créer un groupe.
     */
    @GetMapping("/creer")
    public String creerGroupe() {
        return "creer_groupe";
    }

    /**
     * Valide et enregistre un nouveau groupe.
     *
     * @param groupeDTO Les données du groupe à créer.
     * @return Le nom de la vue à afficher après la validation.
     * @throws RuntimeException Si une erreur survient lors du traitement du logo.
     */
    @PostMapping("/valider")
    public String validerGroupe(@ModelAttribute GroupeDTO groupeDTO) {
        try {
            Groupe groupe = new Groupe();
            groupe.setNomGroupe(groupeDTO.getNomGroupe());
            groupe.setThemeGroupe(groupeDTO.getThemeGroupe());
            groupe.setDescriptionGroupe(groupeDTO.getDescriptionGroupe());

            // Si un logo est fourni, le convertir en tableau de bytes
            if (groupeDTO.getLogoGroupe() != null && !groupeDTO.getLogoGroupe().isEmpty()) {
                groupe.setLogoGroupe(groupeDTO.getLogoGroupe().getBytes());
            }

            // Associer le créateur du groupe à l'utilisateur connecté
            groupe.setCreateurGroupe(utilisateurService.getUtilisateurConnecte());
            return groupeService.enregistrerGroupe(groupe);
        } catch (IOException e) {
            // Gérer les erreurs liées au traitement du logo
            throw new RuntimeException("Erreur lors du traitement du logo du groupe", e);
        }
    }

    /**
     * Permet à un utilisateur de rejoindre un groupe.
     *
     * @param id L'identifiant du groupe à rejoindre.
     * @return Le nom de la vue à afficher après l'opération.
     */
    @PostMapping("/rejoindre/{id}")
    public String rejoindreGroupe(@PathVariable Long id) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurConnecte();
        Groupe groupe = groupeService.trouverGroupeParId(id);

        // Ajouter l'utilisateur au groupe s'il n'est pas déjà membre
        if (groupe != null && !groupe.getMembres().contains(utilisateur)) {
            groupe.getMembres().add(utilisateur);
            utilisateur.getGroupes().add(groupe);
            groupeService.enregistrerGroupe(groupe);
        }

        return "accueil";
    }

    /**
     * Affiche la page d'un groupe.
     *
     * @param model Le modèle contenant les données à afficher dans la vue.
     * @return Le nom de la vue pour afficher la page du groupe.
     */
    @GetMapping("/groupe")
    public String afficherPageGroupe(Model model) {
        Utilisateur utilisateurConnecte = utilisateurService.getUtilisateurConnecte();
        model.addAttribute("utilisateur", utilisateurConnecte);
        return "groupe";
    }

}