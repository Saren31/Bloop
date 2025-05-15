package utcapitole.miage.bloop.controller;


import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.UtilisateurRepository;

import java.util.Optional;

@RestController
@RequestMapping("/profil")
public class ProfilRestController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @PostMapping("/seConnecter")
    public ResponseEntity<String> seConnecter(@RequestParam String email,
                                              @RequestParam String mdp,
                                              HttpSession session) {
        Utilisateur utilisateur = utilisateurRepository.findByEmailUser(email);

        if (utilisateur == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email incorrect");
        }

        if (!utilisateur.getMdpUser().equals(mdp)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Mot de passe incorrect");
        }

        session.setAttribute("utilisateur", utilisateur);
        return ResponseEntity.ok("Connexion réussie !");
    }

    @GetMapping("/monProfil")
    public ResponseEntity<Utilisateur> monProfil(HttpSession session) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");

        if (utilisateur == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(utilisateur);
    }

    @PostMapping("/seDeconnecter")
    public ResponseEntity<String> seDeconnecter(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Déconnexion réussie");
    }
}
