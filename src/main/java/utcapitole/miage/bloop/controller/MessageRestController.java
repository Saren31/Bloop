package utcapitole.miage.bloop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utcapitole.miage.bloop.dto.MessageDTO;
import utcapitole.miage.bloop.model.entity.Message;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.MessageService;
import utcapitole.miage.bloop.service.UtilisateurService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageRestController {

    private final MessageService messageService;
    private final UtilisateurService utilisateurService;

    public MessageRestController(MessageService ms, UtilisateurService us) {
        this.messageService = ms;
        this.utilisateurService = us;
    }

    // Récupère l’historique
    @GetMapping("/history/{destId}")
    public List<MessageDTO> getHistorique(@PathVariable Long destId, Principal principal) {
        Utilisateur expediteur = utilisateurService.getUtilisateurConnecte();
        return messageService.historique(expediteur.getIdUser(), destId);
    }

    // Envoie un message
    @PostMapping("/send")
    public MessageDTO sendMessage(@RequestParam Long destId, @RequestParam String contenu, Principal principal) {
        Utilisateur expediteur = utilisateurService.getUtilisateurConnecte();
        return messageService.envoyerMessage(expediteur.getIdUser(), destId, contenu);
    }
}

