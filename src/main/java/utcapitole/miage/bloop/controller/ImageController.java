package utcapitole.miage.bloop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.UtilisateurService;

@Controller
public class ImageController {

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping("/avatar/{id}")
    public ResponseEntity<byte[]> getAvatar(@PathVariable Long id) {
        Utilisateur utilisateur = utilisateurService.getUtilisateurById(id);

        if (utilisateur != null && utilisateur.getAvatarUser() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(utilisateur.getAvatarUser());
        }

        return ResponseEntity.notFound().build();
    }
}