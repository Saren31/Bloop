package utcapitole.miage.bloop.service;

import org.junit.jupiter.api.Test;
import utcapitole.miage.bloop.dto.MessageDTO;
import utcapitole.miage.bloop.model.entity.Message;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.jpa.MessageRepository;
import utcapitole.miage.bloop.repository.jpa.UtilisateurRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
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

    @Test
    void testToDTO() {
        Message m = new Message();
        m.setId(42L);
        m.setContenu("Test");
        m.setDateEnvoi(LocalDateTime.now());
        Utilisateur exp = new Utilisateur();
        exp.setIdUser(1L);
        exp.setNomUser("Alice");
        Utilisateur dest = new Utilisateur();
        dest.setIdUser(2L);
        m.setExpediteur(exp);
        m.setDestinataire(dest);

        MessageDTO dto = messageService.toDTO(m);

        assertThat(dto.getId()).isEqualTo(42L);
        assertThat(dto.getContenu()).isEqualTo("Test");
        assertThat(dto.getExpediteur().getIdUser()).isEqualTo(1L);
        assertThat(dto.getExpediteur().getNomUser()).isEqualTo("Alice");
        assertThat(dto.getDestinataireId()).isEqualTo(2L);
        assertThat(dto.getDateEnvoi()).isEqualTo(m.getDateEnvoi());
    }

    @Test
    void testSupprimerMessage_Success() {
        Message m = new Message();
        m.setId(10L);
        Utilisateur exp = new Utilisateur();
        exp.setIdUser(1L);
        Utilisateur dest = new Utilisateur();
        dest.setIdUser(2L);
        m.setExpediteur(exp);
        m.setDestinataire(dest);

        when(messageRepository.findById(10L)).thenReturn(Optional.of(m));

        Long result = messageService.supprimerMessage(10L, 1L);

        assertThat(result).isEqualTo(2L);
        verify(messageRepository).deleteById(10L);
    }

    @Test
    void testSupprimerMessage_MessageNonTrouve() {
        when(messageRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> messageService.supprimerMessage(99L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Message non trouvé");
    }

    @Test
    void testSupprimerMessage_NonAutorise() {
        Message m = new Message();
        m.setId(11L);
        Utilisateur exp = new Utilisateur();
        exp.setIdUser(1L);
        Utilisateur dest = new Utilisateur();
        dest.setIdUser(2L);
        m.setExpediteur(exp);
        m.setDestinataire(dest);

        when(messageRepository.findById(11L)).thenReturn(Optional.of(m));

        assertThatThrownBy(() -> messageService.supprimerMessage(11L, 999L))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("Suppression non autorisée");
    }
}