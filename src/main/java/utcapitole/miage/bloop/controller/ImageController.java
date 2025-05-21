package utcapitole.miage.bloop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.UtilisateurService;

import java.io.IOException;

@Controller
public class ImageController {

    private final UtilisateurService utilisateurService;

    @Autowired
    public ImageController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

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

    @PostMapping("/avatar/upload")
    public String uploadAvatar(@RequestParam("avatar") MultipartFile avatarFile,
                               @AuthenticationPrincipal Utilisateur utilisateur) throws IOException {
        if (!avatarFile.isEmpty()) {
            utilisateur.setAvatarUser(avatarFile.getBytes());
            utilisateurService.save(utilisateur);
        }
        return "redirect:/profil/voirProfil";
    }
}