package utcapitole.miage.bloop.service;

import org.springframework.stereotype.Service;
import utcapitole.miage.bloop.dto.MessageDTO;
import utcapitole.miage.bloop.model.entity.Message;
import utcapitole.miage.bloop.repository.jpa.MessageRepository;
import utcapitole.miage.bloop.repository.jpa.UtilisateurRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service pour la gestion des messages.
 * Fournit des méthodes pour envoyer des messages, récupérer l'historique
 * et convertir des entités en DTOs.
 */
@Service
public class MessageService {
    private final MessageRepository repo;
    private final UtilisateurRepository userRepo;

    /**
     * Constructeur pour injecter les dépendances nécessaires.
     *
     * @param repo Référentiel pour la gestion des entités Message.
     * @param userRepo Référentiel pour la gestion des entités Utilisateur.
     */
    public MessageService(MessageRepository repo, UtilisateurRepository userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
    }

    /**
     * Envoie un message d'un expéditeur à un destinataire.
     *
     * @param expId ID de l'expéditeur.
     * @param destId ID du destinataire.
     * @param contenu Contenu du message.
     * @return Un DTO représentant le message envoyé.
     */
    public MessageDTO envoyerMessage(Long expId, Long destId, String contenu) {
        Message m = new Message();
        m.setExpediteur(userRepo.findById(expId).orElseThrow());
        m.setDestinataire(userRepo.findById(destId).orElseThrow());
        m.setContenu(contenu);
        m.setDateEnvoi(LocalDateTime.now());
        repo.save(m);
        return toDTO(m);
    }

    /**
     * Récupère l'historique des messages entre un expéditeur et un destinataire.
     *
     * @param expId ID de l'expéditeur.
     * @param destId ID du destinataire.
     * @return Une liste de DTOs représentant les messages échangés.
     */
    public List<MessageDTO> historique(Long expId, Long destId) {
        return repo.historique(expId, destId).stream().map(this::toDTO).toList();
    }

    /**
     * Convertit une entité Message en DTO.
     *
     * @param m Entité Message à convertir.
     * @return Un DTO représentant le message.
     */
    public MessageDTO toDTO(Message m) {
        MessageDTO dto = new MessageDTO();
        dto.setId(m.getId());
        dto.setContenu(m.getContenu());
        dto.setDateEnvoi(m.getDateEnvoi());
        // Résumé de l'expéditeur
        MessageDTO.UtilisateurSummary u = new MessageDTO.UtilisateurSummary();
        u.setIdUser(m.getExpediteur().getIdUser());
        u.setNomUser(m.getExpediteur().getNomUser());
        dto.setExpediteur(u);
        dto.setDestinataireId(m.getDestinataire().getIdUser());
        return dto;
    }

    public Long supprimerMessage(Long messageId, Long expId) {
        Message msg = repo.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message non trouvé : " + messageId));
        if (msg.getExpediteur().getIdUser() != expId) {
            throw new SecurityException("Suppression non autorisée");
        }
        Long destinataireId = msg.getDestinataire().getIdUser();
        repo.deleteById(messageId);
        return destinataireId;
    }
}