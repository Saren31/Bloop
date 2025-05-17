// src/test/java/utcapitole/miage/bloop/service/MessageServiceTest.java
package utcapitole.miage.bloop.service;

import org.junit.jupiter.api.Test;
import utcapitole.miage.bloop.dto.MessageDTO;
import utcapitole.miage.bloop.model.entity.Message;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.MessageRepository;
import utcapitole.miage.bloop.repository.UtilisateurRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MessageServiceTest {

    private final MessageRepository messageRepository = mock(MessageRepository.class);
    private final UtilisateurRepository utilisateurRepository = mock(UtilisateurRepository.class);
    private final MessageService messageService = new MessageService(messageRepository, utilisateurRepository);

    @Test
    void testEnvoyerMessage() {
        Utilisateur exp = new Utilisateur();
        exp.setIdUser(1L);
        Utilisateur dest = new Utilisateur();
        dest.setIdUser(2L);

        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(exp));
        when(utilisateurRepository.findById(2L)).thenReturn(Optional.of(dest));
        when(messageRepository.save(any(Message.class))).thenAnswer(i -> i.getArgument(0));

        MessageDTO dto = messageService.envoyerMessage(1L, 2L, "Hello");

        assertThat(dto.getContenu()).isEqualTo("Hello");
        assertThat(dto.getExpediteur().getIdUser()).isEqualTo(1L);
        assertThat(dto.getDestinataireId()).isEqualTo(2L);
        verify(messageRepository).save(any(Message.class));
    }

    @Test
    void testHistorique() {
        Message m = new Message();
        Utilisateur exp = new Utilisateur();
        exp.setIdUser(1L);
        Utilisateur dest = new Utilisateur();
        dest.setIdUser(2L);
        m.setExpediteur(exp);
        m.setDestinataire(dest);
        m.setContenu("Salut");

        when(messageRepository.historique(1L, 2L)).thenReturn(List.of(m));

        List<MessageDTO> dtos = messageService.historique(1L, 2L);

        assertThat(dtos).hasSize(1);
        assertThat(dtos.get(0).getContenu()).isEqualTo("Salut");
    }
}