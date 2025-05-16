package utcapitole.miage.bloop.service;

import org.junit.jupiter.api.Test;
import utcapitole.miage.bloop.model.entity.Groupe;
import utcapitole.miage.bloop.repository.GroupeRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GroupeServiceTest {

    private final GroupeRepository groupeRepository = mock(GroupeRepository.class);
    private final GroupeService groupeService = new GroupeService(groupeRepository);

    @Test
    void testEnregistrerGroupe_Succes() {
        Groupe groupe = new Groupe();
        groupe.setNomGroupe("Test Groupe");

        when(groupeRepository.save(groupe)).thenReturn(groupe);

        String result = groupeService.enregistrerGroupe(groupe);

        assertThat(result).isEqualTo("accueil");
        verify(groupeRepository).save(groupe);
    }

    @Test
    void testTrouverGroupeParId_Succes() {
        Groupe groupe = new Groupe();
        groupe.setIdGroupe(1L);

        when(groupeRepository.findById(1L)).thenReturn(Optional.of(groupe));

        Groupe result = groupeService.trouverGroupeParId(1L);

        assertThat(result).isNotNull();
        assertThat(result.getIdGroupe()).isEqualTo(1L);
        verify(groupeRepository).findById(1L);
    }

    @Test
    void testTrouverGroupeParId_Echec() {
        when(groupeRepository.findById(1L)).thenReturn(Optional.empty());

        Groupe result = groupeService.trouverGroupeParId(1L);

        assertThat(result).isNull();
        verify(groupeRepository).findById(1L);
    }
}