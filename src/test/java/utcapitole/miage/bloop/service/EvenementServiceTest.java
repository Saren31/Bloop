package utcapitole.miage.bloop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utcapitole.miage.bloop.model.entity.Evenement;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.jpa.EvenementRepository;
import utcapitole.miage.bloop.repository.jpa.UtilisateurRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class EvenementServiceTest {

    private EvenementRepository evenementRepository;
    private EvenementService evenementService;
    private UtilisateurRepository utilisateurRepository;

    @BeforeEach
    void setUp() {
        evenementRepository = mock(EvenementRepository.class);
        utilisateurRepository = mock(UtilisateurRepository.class);
        evenementService = new EvenementService(evenementRepository, utilisateurRepository);
    }

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

    @Test
    void testGetEvenementParId() {
        Evenement e = new Evenement();
        when(evenementRepository.findById(2L)).thenReturn(java.util.Optional.of(e));
        Evenement result = evenementService.getEvenementParId(2L);
        assertThat(result).isEqualTo(e);
    }

    @Test
    void testGetEvenementParId_NotFound() {
        when(evenementRepository.findById(3L)).thenReturn(java.util.Optional.empty());
        Evenement result = evenementService.getEvenementParId(3L);
        assertThat(result).isNull();
    }

    @Test
    void testInscrireUtilisateur() {
        Evenement e = new Evenement();
        Utilisateur u = new Utilisateur();
        u.setIdUser(0L);
        e.setInscrits(new HashSet<>());
        when(utilisateurRepository.findById(0L)).thenReturn(java.util.Optional.of(u));
        evenementService.inscrireUtilisateur(e, u);
        assertThat(e.getInscrits()).contains(u);
        verify(evenementRepository).save(e);
    }

    @Test
    void testInscrireUtilisateur_DejaInscrit() {
        Evenement e = new Evenement();
        Utilisateur u = new Utilisateur();
        u.setIdUser(0L);
        Set<Utilisateur> inscrits = new HashSet<>();
        inscrits.add(u);
        e.setInscrits(inscrits);
        when(utilisateurRepository.findById(0L)).thenReturn(java.util.Optional.of(u));
        evenementService.inscrireUtilisateur(e, u);
        verify(evenementRepository, never()).save(e);
    }

    @Test
    void testRetirerUtilisateur() {
        Evenement e = new Evenement();
        Utilisateur u = new Utilisateur();
        u.setIdUser(0L);
        Set<Utilisateur> inscrits = new HashSet<>();
        inscrits.add(u);
        e.setInscrits(inscrits);
        evenementService.retirerUtilisateur(e, u);
        assertThat(e.getInscrits()).doesNotContain(u);
        verify(evenementRepository).save(e);
    }

    @Test
    void testRetirerUtilisateur_NonInscrit() {
        Evenement e = new Evenement();
        Utilisateur u = new Utilisateur();
        e.setInscrits(new HashSet<>());
        evenementService.retirerUtilisateur(e, u);
        verify(evenementRepository, never()).save(e);
    }

    @Test
    void testMarquerInteresse() {
        Evenement e = new Evenement();
        Utilisateur u = new Utilisateur();
        e.setInteresses(new HashSet<>());
        evenementService.marquerInteresse(e, u);
        assertThat(e.getInteresses()).contains(u);
        verify(evenementRepository).save(e);
    }

    @Test
    void testMarquerInteresse_DejaInteresse() {
        Evenement e = new Evenement();
        Utilisateur u = new Utilisateur();
        Set<Utilisateur> interesses = new HashSet<>();
        interesses.add(u);
        e.setInteresses(interesses);
        evenementService.marquerInteresse(e, u);
        verify(evenementRepository, never()).save(e);
    }

    @Test
    void testRetirerInteresse() {
        Evenement e = new Evenement();
        Utilisateur u = new Utilisateur();
        Set<Utilisateur> interesses = new HashSet<>();
        interesses.add(u);
        e.setInteresses(interesses);
        evenementService.retirerInteresse(e, u);
        assertThat(e.getInteresses()).doesNotContain(u);
        verify(evenementRepository).save(e);
    }

    @Test
    void testRetirerInteresse_NonInteresse() {
        Evenement e = new Evenement();
        Utilisateur u = new Utilisateur();
        e.setInteresses(new HashSet<>());
        evenementService.retirerInteresse(e, u);
        verify(evenementRepository, never()).save(e);
    }

    @Test
    void testEstInscrit() {
        Evenement e = new Evenement();
        Utilisateur u = new Utilisateur();
        Set<Utilisateur> inscrits = new HashSet<>();
        inscrits.add(u);
        e.setInscrits(inscrits);
        assertThat(evenementService.estInscrit(e, u)).isTrue();
    }

    @Test
    void testEstInscrit_Faux() {
        Evenement e = new Evenement();
        Utilisateur u = new Utilisateur();
        e.setInscrits(new HashSet<>());
        assertThat(evenementService.estInscrit(e, u)).isFalse();
    }

    @Test
    void testEstInteresse() {
        Evenement e = new Evenement();
        Utilisateur u = new Utilisateur();
        Set<Utilisateur> interesses = new HashSet<>();
        interesses.add(u);
        e.setInteresses(interesses);
        assertThat(evenementService.estInteresse(e, u)).isTrue();
    }

    @Test
    void testEstInteresse_Faux() {
        Evenement e = new Evenement();
        Utilisateur u = new Utilisateur();
        e.setInteresses(new HashSet<>());
        assertThat(evenementService.estInteresse(e, u)).isFalse();
    }

    @Test
    void testGetEvenementsOuUtilisateurEstInscrit() {
        Utilisateur u = new Utilisateur();
        u.setIdUser(1L);

        Evenement e1 = new Evenement();
        e1.setDateDebut(new java.util.Date(2000000));
        Evenement e2 = new Evenement();
        e2.setDateDebut(new java.util.Date(1000000));
        List<Evenement> evenements = List.of(e1, e2);

        when(evenementRepository.findByInscrits_IdUser(1L)).thenReturn(evenements);

        List<Evenement> result = evenementService.getEvenementsOuUtilisateurEstInscrit(1L);

        assertThat(result).containsExactly(e2, e1); // trié du plus proche au plus loin
    }
}