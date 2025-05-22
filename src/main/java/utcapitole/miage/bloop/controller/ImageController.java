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

/**
 * Contrôleur pour gérer les opérations liées aux images des utilisateurs, comme les avatars.
 */
@Controller
public class ImageController {

    private final UtilisateurService utilisateurService;

    /**
     * Constructeur pour injecter le service utilisateur.
     *
     * @param utilisateurService Service pour la gestion des utilisateurs.
     */
    @Autowired
    public ImageController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    /**
     * Récupère l'avatar d'un utilisateur spécifique.
     *
     * @param id L'identifiant de l'utilisateur.
     * @return Une réponse contenant l'image de l'avatar au format JPEG, ou une réponse 404 si l'utilisateur ou l'avatar n'existe pas.
     */
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

    /**
     * Permet à un utilisateur connecté de téléverser un avatar.
     *
     * @param avatarFile Le fichier de l'avatar téléversé.
     * @param utilisateur L'utilisateur actuellement authentifié.
     * @return Une redirection vers la page de profil après le téléversement.
     * @throws IOException Si une erreur survient lors de la lecture du fichier.
     */
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