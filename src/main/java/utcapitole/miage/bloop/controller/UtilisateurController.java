package utcapitole.miage.bloop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.UtilisateurService;

import java.util.List;

/**
 * Contrôleur REST pour gérer les opérations liées aux utilisateurs.
 */
@RestController
@RequestMapping("/utilisateurs")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    /**
     * Constructeur pour injecter le service des utilisateurs.
     *
     * @param utilisateurService Le service pour interagir avec les utilisateurs.
     */
    @Autowired
    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    /**
     * Gère les requêtes GET pour récupérer la liste de tous les utilisateurs.
     *
     * @return Une réponse HTTP contenant la liste des utilisateurs.
     */
    @GetMapping
    public ResponseEntity<List<Utilisateur>> listerUtilisateurs() {
        return ResponseEntity.ok(utilisateurService.recupererTousLesUtilisateurs());
    }

    /**
     * Gère les requêtes GET pour récupérer un utilisateur spécifique par son identifiant.
     *
     * @param id L'identifiant de l'utilisateur à récupérer.
     * @return Une réponse HTTP contenant l'utilisateur si trouvé, ou une réponse 404 si non trouvé.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> recupererUtilisateur(@PathVariable Long id) {
        return utilisateurService.recupererUtilisateurParId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/recherche_pseudo")
    public ResponseEntity<List<Utilisateur>> rechercherParPseudo(@RequestParam String pseudo) {
        List<Utilisateur> utilisateurs = utilisateurService.rechercherParPseudo(pseudo);
        return ResponseEntity.ok(utilisateurs);
    }
}