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
    @Autowired
    private MessageService messageService;
    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping("/history/{destId}")
    public List<Message> getHistorique(@PathVariable Long destId, Principal principal) {
        Long expId = utilisateurService.findByEmail(principal.getName()).getIdUser();
        return messageService.getHistorique(expId, destId);
    }

    @PostMapping("/send")
    public MessageDTO sendMessage(@RequestParam Long destId, @RequestParam String contenu, Principal principal) {
        // Récupère l'utilisateur courant (expéditeur)
        Utilisateur expediteur = utilisateurService.getUtilisateurConnecte();
        Message m = messageService.envoyerMessage(expediteur.getIdUser(), destId, contenu);
        return messageService.toDTO(m); // <- assure que tu retournes un MessageDTO et pas un Message (sinon le frontend ne reçoit pas le bon objet)
    }

}
