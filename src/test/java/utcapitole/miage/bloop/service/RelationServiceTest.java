package utcapitole.miage.bloop.service;

import org.junit.jupiter.api.Test;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.jpa.UtilisateurRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RelationServiceTest {

    private final UtilisateurRepository utilisateurRepository = mock(UtilisateurRepository.class);
    private final GraphSyncService graphSyncService = mock(GraphSyncService.class);
    private final RelationService relationService = new RelationService(utilisateurRepository, graphSyncService);

    @Test
    void testDemandeSucces() {
        Utilisateur u1 = new Utilisateur(); u1.setIdUser(1L);
        Utilisateur u2 = new Utilisateur(); u2.setIdUser(2L);

        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(u1));
        when(utilisateurRepository.findById(2L)).thenReturn(Optional.of(u2));

        String result = relationService.envoyerDemandeAmitie(1L, 2L);

        assertThat(result).contains("succès");
        verify(utilisateurRepository).save(u1);
        verify(utilisateurRepository).save(u2);
    }

    @Test
    void testMemeUtilisateur() {
        String result = relationService.envoyerDemandeAmitie(1L, 1L);
        assertThat(result).contains("Vous ne pouvez pas");
    }

    @Test
    void testUtilisateurInexistant() {
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.empty());

        String result = relationService.envoyerDemandeAmitie(1L, 2L);
        assertThat(result).contains("n'existe pas");
    }

    @Test
    void testDemandeDejaEnvoyee() {
        Utilisateur u1 = new Utilisateur(); u1.setIdUser(1L);
        Utilisateur u2 = new Utilisateur(); u2.setIdUser(2L);

        u1.getDemandesEnvoyees().add(u2);

        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(u1));
        when(utilisateurRepository.findById(2L)).thenReturn(Optional.of(u2));

        String result = relationService.envoyerDemandeAmitie(1L, 2L);
        assertThat(result).contains("déjà envoyé");
    }

    @Test
    void testAccepterDemandeAmitie() {
        Utilisateur u1 = new Utilisateur(); u1.setIdUser(1L);
        Utilisateur u2 = new Utilisateur(); u2.setIdUser(2L);

        u1.getDemandesRecues().add(u2);
        u2.getDemandesEnvoyees().add(u1);

        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(u1));
        when(utilisateurRepository.findById(2L)).thenReturn(Optional.of(u2));

        String result = relationService.gererDemandeAmitie(1L, 2L, true);

        assertThat(result).contains("acceptée");
        assertThat(u1.getAmis()).contains(u2);
        assertThat(u2.getAmis()).contains(u1);
        verify(utilisateurRepository, times(2)).save(any(Utilisateur.class));
    }

    @Test
    void testRefuserDemandeAmitie() {
        Utilisateur u1 = new Utilisateur(); u1.setIdUser(1L);
        Utilisateur u2 = new Utilisateur(); u2.setIdUser(2L);

        u1.getDemandesRecues().add(u2);
        u2.getDemandesEnvoyees().add(u1);

        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(u1));
        when(utilisateurRepository.findById(2L)).thenReturn(Optional.of(u2));

        String result = relationService.gererDemandeAmitie(1L, 2L, false);

        assertThat(result).contains("refusée");
        assertThat(u1.getAmis()).doesNotContain(u2);
        assertThat(u2.getAmis()).doesNotContain(u1);
        verify(utilisateurRepository, times(2)).save(any(Utilisateur.class));
    }

    @Test
    void testSupprimerAmiSucces() {
        Utilisateur u1 = new Utilisateur(); u1.setIdUser(1L);
        Utilisateur u2 = new Utilisateur(); u2.setIdUser(2L);

        u1.getAmis().add(u2);
        u2.getAmis().add(u1);

        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(u1));
        when(utilisateurRepository.findById(2L)).thenReturn(Optional.of(u2));

        String result = relationService.supprimerAmi(1L, 2L);

        assertThat(result).contains("succès");
        assertThat(u1.getAmis()).doesNotContain(u2);
        assertThat(u2.getAmis()).doesNotContain(u1);
        verify(utilisateurRepository, times(2)).save(any(Utilisateur.class));
    }

    @Test
    void testSupprimerAmiNonExistant() {
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.empty());

        String result = relationService.supprimerAmi(1L, 2L);
        assertThat(result).contains("Utilisateur non trouvé");
    }

    @Test
    void testSupprimerAmiPasDansListe() {
        Utilisateur u1 = new Utilisateur(); u1.setIdUser(1L);
        Utilisateur u2 = new Utilisateur(); u2.setIdUser(2L);

        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(u1));
        when(utilisateurRepository.findById(2L)).thenReturn(Optional.of(u2));

        String result = relationService.supprimerAmi(1L, 2L);
        assertThat(result).contains("n’est pas dans votre liste");
    }

    @Test
    void testGetListeAmis_UtilisateurExistant() {
        // Configuration des entités
        Utilisateur u = new Utilisateur();
        u.setIdUser(1L);
        Utilisateur ami = new Utilisateur();
        ami.setIdUser(2L);
        ami.setNomUser("Doe");
        ami.setPrenomUser("John");
        ami.setPseudoUser("johndoe");
        ami.setEmailUser("john@ut-capitole.fr");
        u.getAmis().add(ami);

        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(u));

        // Appel de la méthode
        var amisDTO = relationService.getListeAmis(1L);

        // Vérification
        assertThat(amisDTO).hasSize(1);
        assertThat(amisDTO.get(0).getIdUser()).isEqualTo(2L);
        assertThat(amisDTO.get(0).getNomUser()).isEqualTo("Doe");
        assertThat(amisDTO.get(0).getPrenomUser()).isEqualTo("John");
        assertThat(amisDTO.get(0).getPseudoUser()).isEqualTo("johndoe");
        assertThat(amisDTO.get(0).getEmailUser()).isEqualTo("john@ut-capitole.fr");
    }

    @Test
    void testGetListeAmis_UtilisateurInexistant() {
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.empty());

        var amis = relationService.getListeAmis(1L);
        assertThat(amis).isEmpty();
    }

    @Test
    void testGererDemandeAmitie_DemandeInexistante() {
        Utilisateur receveur = new Utilisateur(); receveur.setIdUser(1L);
        Utilisateur envoyeur = new Utilisateur(); envoyeur.setIdUser(2L);

        // Pas de demande dans la liste
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(receveur));
        when(utilisateurRepository.findById(2L)).thenReturn(Optional.of(envoyeur));

        String result = relationService.gererDemandeAmitie(1L, 2L, true);
        assertThat(result).contains("Aucune demande");
    }
}