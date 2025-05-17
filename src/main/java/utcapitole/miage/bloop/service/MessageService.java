package utcapitole.miage.bloop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utcapitole.miage.bloop.dto.MessageDTO;
import utcapitole.miage.bloop.model.entity.Message;
import utcapitole.miage.bloop.repository.MessageRepository;
import utcapitole.miage.bloop.repository.UtilisateurRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    // Sauvegarder un message
    public Message envoyerMessage(Long expId, Long destId, String contenu) {
        Message m = new Message();
        m.setExpediteur(utilisateurRepository.findById(expId).orElseThrow());
        m.setDestinataire(utilisateurRepository.findById(destId).orElseThrow());
        m.setContenu(contenu);
        m.setDateEnvoi(LocalDateTime.now());
        return messageRepository.save(m);
    }

    // Cette méthode convertit Message → MessageDTO (exemple simple)
    public MessageDTO toDTO(Message m) {
        MessageDTO dto = new MessageDTO();
        dto.setId(m.getId());
        dto.setContenu(m.getContenu());
        dto.setDateEnvoi(m.getDateEnvoi());
        // Pour l’affichage simple côté JS :
        MessageDTO.UtilisateurSummary exp = new MessageDTO.UtilisateurSummary();
        exp.setIdUser(m.getExpediteur().getIdUser());
        exp.setNomUser(m.getExpediteur().getNomUser());
        dto.setExpediteur(exp);
        // Pour le JS ou d’autres traitements
        dto.setExpediteurId(m.getExpediteur().getIdUser());
        dto.setDestinataireId(m.getDestinataire().getIdUser());
        return dto;
    }


    // Récupérer l'historique
    public List<Message> getHistorique(Long expId, Long destId) {
        return messageRepository.findByExpediteur_IdUserAndDestinataire_IdUserOrExpediteur_IdUserAndDestinataire_IdUserOrderByDateEnvoiAsc(
                expId, destId, destId, expId
        );
    }
}
