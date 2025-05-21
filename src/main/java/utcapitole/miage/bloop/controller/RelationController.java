package utcapitole.miage.bloop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utcapitole.miage.bloop.dto.UtilisateurDTO;
import utcapitole.miage.bloop.service.RelationService;

import java.util.List;

/**
 * Contrôleur REST pour gérer les relations entre utilisateurs.
 */
@RestController
@RequestMapping("/relations")
public class RelationController {

    private final RelationService relationService;

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
     * Envoie une demande d'amitié d'un utilisateur à un autre.
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

    /**
     * Accepte une demande d'amitié reçue par un utilisateur.
     *
     * @param idReceveur L'identifiant de l'utilisateur qui accepte la demande.
     * @param idEnvoyeur L'identifiant de l'utilisateur qui a envoyé la demande.
     * @return Une réponse HTTP contenant un message de succès ou d'erreur.
     */
    @PostMapping("/accepter")
    public ResponseEntity<String> accepterDemandeAmitie(@RequestParam Long idReceveur, @RequestParam Long idEnvoyeur) {
        String resultat = relationService.gererDemandeAmitie(idReceveur, idEnvoyeur, true);
        if (resultat.contains("acceptée")) {
            return ResponseEntity.ok(resultat);
        } else {
            return ResponseEntity.badRequest().body(resultat);
        }
    }

    /**
     * Refuse une demande d'amitié reçue par un utilisateur.
     *
     * @param idReceveur L'identifiant de l'utilisateur qui refuse la demande.
     * @param idEnvoyeur L'identifiant de l'utilisateur qui a envoyé la demande.
     * @return Une réponse HTTP contenant un message de succès ou d'erreur.
     */
    @PostMapping("/refuser")
    public ResponseEntity<String> refuserDemandeAmitie(@RequestParam Long idReceveur, @RequestParam Long idEnvoyeur) {
        String resultat = relationService.gererDemandeAmitie(idReceveur, idEnvoyeur, false);
        if (resultat.contains("refusée")) {
            return ResponseEntity.ok(resultat);
        } else {
            return ResponseEntity.badRequest().body(resultat);
        }
    }

    /**
     * Récupère la liste des amis d'un utilisateur.
     *
     * @param idUser L'identifiant de l'utilisateur.
     * @return Une réponse HTTP contenant la liste des amis de l'utilisateur.
     */
    @GetMapping("/amis")
    public ResponseEntity<List<UtilisateurDTO>> voirListeAmis(@RequestParam Long idUser) {
        List<UtilisateurDTO> amis = relationService.getListeAmis(idUser);
        return ResponseEntity.ok(amis);
    }

    /**
     * Supprime un ami de la liste d'amis d'un utilisateur.
     *
     * @param idUser L'identifiant de l'utilisateur.
     * @param idAmi L'identifiant de l'ami à supprimer.
     * @return Une réponse HTTP contenant un message de succès ou d'erreur.
     */
    @DeleteMapping("/supprimer")
    public ResponseEntity<String> supprimerAmi(@RequestParam Long idUser, @RequestParam Long idAmi) {
        String resultat = relationService.supprimerAmi(idUser, idAmi);
        if (resultat.contains("succès")) {
            return ResponseEntity.ok(resultat);
        } else {
            return ResponseEntity.badRequest().body(resultat);
        }
    }
}