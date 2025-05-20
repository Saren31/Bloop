package utcapitole.miage.bloop.service;

import org.junit.jupiter.api.Test;
import utcapitole.miage.bloop.model.entity.Evenement;
import utcapitole.miage.bloop.repository.jpa.EvenementRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class EvenementServiceTest {

    private final EvenementRepository evenementRepository = mock(EvenementRepository.class);
    private final EvenementService evenementService = new EvenementService(evenementRepository);


    @Test
    void testCreerEvenement() {
        Evenement e = new Evenement();
        evenementService.creerEvenement(e);
        verify(evenementRepository).save(e);
    }

    @Test
    void testGetEvenementsParOrganisateur() {
        Evenement e = new Evenement();
        when(evenementRepository.findByOrganisateur_IdUser(1L)).thenReturn(List.of(e));
        List<Evenement> result = evenementService.getEvenementsParOrganisateur(1L);
        assertThat(result).containsExactly(e);
    }
}