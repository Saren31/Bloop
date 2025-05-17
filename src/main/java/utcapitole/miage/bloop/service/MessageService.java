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
    private final MessageRepository repo;
    private final UtilisateurRepository userRepo;

    public MessageService(MessageRepository repo, UtilisateurRepository userRepo) {
        this.repo = repo; this.userRepo = userRepo;
    }

    public MessageDTO envoyerMessage(Long expId, Long destId, String contenu) {
        Message m = new Message();
        m.setExpediteur(userRepo.findById(expId).orElseThrow());
        m.setDestinataire(userRepo.findById(destId).orElseThrow());
        m.setContenu(contenu);
        m.setDateEnvoi(LocalDateTime.now());
        repo.save(m);
        return toDTO(m);
    }

    public List<MessageDTO> historique(Long expId, Long destId) {
        return repo.historique(expId, destId).stream().map(this::toDTO).toList();
    }

    public MessageDTO toDTO(Message m) {
        MessageDTO dto = new MessageDTO();
        dto.setId(m.getId());
        dto.setContenu(m.getContenu());
        dto.setDateEnvoi(m.getDateEnvoi());
        // Résumé expéditeur
        MessageDTO.UtilisateurSummary u = new MessageDTO.UtilisateurSummary();
        u.setIdUser(m.getExpediteur().getIdUser());
        u.setNomUser(m.getExpediteur().getNomUser());
        dto.setExpediteur(u);
        dto.setDestinataireId(m.getDestinataire().getIdUser());
        return dto;
    }
}

