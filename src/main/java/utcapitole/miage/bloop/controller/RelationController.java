package utcapitole.miage.bloop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utcapitole.miage.bloop.service.RelationService;

/**
 * Contrôleur REST pour gérer les relations entre utilisateurs.
 */
@RestController
@RequestMapping("/relations")
public class RelationController {

    private RelationService relationService;

    /**
     * Constructeur pour injecter le service de gestion des relations.
     *
     * @param relationService Service utilisé pour gérer les relations entre utilisateurs.
     */
    @Autowired
    public RelationController(RelationService relationService) {
        this.relationService = relationService;
    }

    /**
     * Gère les requêtes POST pour envoyer une demande d'amitié.
     *
     * @param idEnvoyeur L'identifiant de l'utilisateur qui envoie la demande.
     * @param idReceveur L'identifiant de l'utilisateur qui reçoit la demande.
     * @return Une réponse HTTP contenant un message de succès ou d'erreur.
     */
    @PostMapping("/demande")
    public ResponseEntity<String> envoyerDemandeAmitie(@RequestParam Long idEnvoyeur, @RequestParam Long idReceveur) {

        String resultat = relationService.envoyerDemandeAmitie(idEnvoyeur, idReceveur);

        if (resultat.contains("succès")) {
            return ResponseEntity.ok(resultat);
        } else {
            return ResponseEntity.badRequest().body(resultat);
        }
    }

    @PostMapping("/accepter")
    public ResponseEntity<String> accepterDemandeAmitie(@RequestParam Long idReceveur, @RequestParam Long idEnvoyeur) {
        String resultat = relationService.accepterDemandeAmitie(idReceveur, idEnvoyeur);
        if (resultat.contains("acceptée")) {
            return ResponseEntity.ok(resultat);
        } else {
            return ResponseEntity.badRequest().body(resultat);
        }
    }
}