package utcapitole.miage.bloop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.UtilisateurRepository;
import utcapitole.miage.bloop.service.UtilisateurService;

import java.util.Optional;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private UtilisateurService utilisateurService;

    // Crée deux utilisateurs de test
    @GetMapping("/init")
    public ResponseEntity<String> initUtilisateurs() {
        Utilisateur u1 = new Utilisateur();
        u1.setNomUser("Alice");
        u1.setPrenomUser("Test");
        u1.setEmailUser("alice@ut-capitole.fr");
        u1.setMdpUser("123");
        u1.setPseudoUser("alice");
        u1.setTelUser("0000000001");
        u1.setVisibiliteUser(true);
        utilisateurRepository.save(u1);

        Utilisateur u2 = new Utilisateur();
        u2.setNomUser("Bob");
        u2.setPrenomUser("Test");
        u2.setEmailUser("bob@ut-capitole.fr");
        u2.setMdpUser("123");
        u2.setPseudoUser("bob");
        u2.setTelUser("0000000002");
        u2.setVisibiliteUser(true);
        utilisateurRepository.save(u2);
        return ResponseEntity.ok("Utilisateurs créés avec succès : Alice (" + u1.getIdUser() + "), Bob (" + u2.getIdUser() +")");

    }

    @PostMapping("/{idEnvoyeur}/demande/{idReceveur}")
    public ResponseEntity<String> envoyerDemandeAmitie(
            @PathVariable Long idEnvoyeur,
            @PathVariable Long idReceveur) {

        String resultat = utilisateurService.envoyerDemandeAmitie(idEnvoyeur, idReceveur);

        if (resultat.contains("succès")) {
            return ResponseEntity.ok(resultat);
        } else {
            return ResponseEntity.badRequest().body(resultat);
        }
    }
}