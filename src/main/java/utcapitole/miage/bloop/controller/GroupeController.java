package utcapitole.miage.bloop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import utcapitole.miage.bloop.dto.GroupeDTO;
import utcapitole.miage.bloop.model.entity.Groupe;
import utcapitole.miage.bloop.service.GroupeService;
import utcapitole.miage.bloop.service.UtilisateurService;

import java.io.IOException;

@Controller
@RequestMapping("/groupes")
public class GroupeController {

    private final GroupeService groupeService;
    private final UtilisateurService utilisateurService;

    @Autowired
    public GroupeController(GroupeService groupeService, UtilisateurService utilisateurService) {
        this.groupeService = groupeService;
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/creer")
    public String creerGroupe() {
        return "creer_groupe";
    }

    @PostMapping("/valider")
    public String validerGroupe(@ModelAttribute GroupeDTO groupeDTO) {
        Groupe groupe = new Groupe();
        groupe.setNomGroupe(groupeDTO.getNomGroupe());
        groupe.setThemeGroupe(groupeDTO.getThemeGroupe());
        groupe.setDescriptionGroupe(groupeDTO.getDescriptionGroupe());

        // Convertir le fichier MultipartFile en tableau de bytes
        if (groupeDTO.getLogoGroupe() != null && !groupeDTO.getLogoGroupe().isEmpty()) {
            try {
                groupe.setLogoGroupe(groupeDTO.getLogoGroupe().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                // Gérer l'erreur
            }
        }

        groupe.setCreateurGroupe(utilisateurService.getUtilisateurConnecte());
        return groupeService.enregistrerGroupe(groupe);
    }
}
