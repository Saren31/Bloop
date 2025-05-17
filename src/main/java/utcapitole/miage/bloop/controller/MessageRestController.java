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

/**
 * Contrôleur REST pour gérer les opérations liées aux messages.
 * Fournit des points d'entrée pour récupérer l'historique des messages
 * et envoyer de nouveaux messages.
 */
@RestController
@RequestMapping("/api/messages")
public class MessageRestController {

    private final MessageService messageService;
    private final UtilisateurService utilisateurService;

    /**
     * Constructeur pour injecter les services nécessaires.
     *
     * @param ms Service pour la gestion des messages.
     * @param us Service pour la gestion des utilisateurs.
     */
    public MessageRestController(MessageService ms, UtilisateurService us) {
        this.messageService = ms;
        this.utilisateurService = us;
    }

    /**
     * Récupère l'historique des messages entre l'utilisateur connecté
     * et un destinataire spécifique.
     *
     * @param destId ID du destinataire avec lequel l'historique est demandé.
     * @param principal Objet représentant l'utilisateur actuellement connecté.
     * @return Une liste de DTOs représentant les messages échangés.
     */
    @GetMapping("/history/{destId}")
    public List<MessageDTO> getHistorique(@PathVariable Long destId, Principal principal) {
        Utilisateur expediteur = utilisateurService.getUtilisateurConnecte();
        return messageService.historique(expediteur.getIdUser(), destId);
    }

    /**
     * Envoie un message d'un utilisateur connecté à un destinataire.
     *
     * @param destId ID du destinataire du message.
     * @param contenu Contenu du message à envoyer.
     * @param principal Objet représentant l'utilisateur actuellement connecté.
     * @return Un DTO représentant le message envoyé.
     */
    @PostMapping("/send")
    public MessageDTO sendMessage(@RequestParam Long destId, @RequestParam String contenu, Principal principal) {
        Utilisateur expediteur = utilisateurService.getUtilisateurConnecte();
        return messageService.envoyerMessage(expediteur.getIdUser(), destId, contenu);
    }
}