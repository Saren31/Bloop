package utcapitole.miage.bloop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.repository.UtilisateurRepository;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UtilisateurServiceTest {
// On crée un faux repository. Il va simuler les accès à la base de données
    @Mock
    private UtilisateurRepository utilisateurRepository;
//Mockito va créer une instance réelle de UtilisateurService
//et lui injecter automatiquement le utilisateurrepository mocke dans le champ correspondant
    @InjectMocks
    private UtilisateurService utilisateurService;

    @Test
    void testEnvoyerDemande_Succes() {
        //d'abord on cree des utilisateur et une liste vide des demandes envoye et recue
        Utilisateur u1 = new Utilisateur();
        u1.setIdUser(1L);
        u1.setDemandesEnvoyees(new ArrayList<>());

        Utilisateur u2 = new Utilisateur();
        u2.setIdUser(2L);
        u2.setDemandesRecues(new ArrayList<>());
//on simule que le repository retrouve bien les deux utilisateures quand il les cherche par id
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(u1));
        when(utilisateurRepository.findById(2L)).thenReturn(Optional.of(u2));

        String resultat = utilisateurService.envoyerDemandeAmitie(1L, 2L);
//vérifie que la réponse texte est correcte
        assertEquals("Demande d’amitié envoyée avec succès.", resultat);
        //vérifie que u2 a bien été ajouté dans la liste des demandes de u1
        assertTrue(u1.getDemandesEnvoyees().contains(u2));

        verify(utilisateurRepository).save(u1);
        verify(utilisateurRepository).save(u2);
    }

    @Test
    void testDemandeDejaEnvoyee() {
        Utilisateur u1 = new Utilisateur();
        Utilisateur u2 = new Utilisateur();
        u1.setIdUser(1L);
        u2.setIdUser(2L);

        u1.setDemandesEnvoyees(new ArrayList<>());
        //On simule que la demande a déjà été faite
        u1.getDemandesEnvoyees().add(u2);

        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(u1));
        when(utilisateurRepository.findById(2L)).thenReturn(Optional.of(u2));

        String resultat = utilisateurService.envoyerDemandeAmitie(1L, 2L);
//on attend une réponse erreur et aucune sauvegarde ne doit etre appelee
        assertEquals("Demande déjà envoyée.", resultat);
        verify(utilisateurRepository, never()).save(any());
    }

    @Test
    void testDemandeASoiMeme() {
        String resultat = utilisateurService.envoyerDemandeAmitie(1L, 1L);
        assertEquals("Vous ne pouvez pas vous envoyer une demande à vous-même.", resultat);
    }

    @Test
    void testUtilisateurInexistant() {
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.empty());

        String resultat = utilisateurService.envoyerDemandeAmitie(1L, 2L);
        assertEquals("Utilisateur non trouvé.", resultat);
    }



    // --------------------- US31 Pour créer un post -------------------------//
    @Test
    void testGetUtilisateurParId() {
        Utilisateur user = new Utilisateur();
        user.setIdUser(1L);
        user.setNomUser("Dupont");

        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(user));

        Utilisateur result = utilisateurService.getUtilisateurParId(1L);

        assertThat(result).isNotNull();
        assertThat(result.getNomUser()).isEqualTo("Dupont");
        verify(utilisateurRepository, times(1)).findById(1L);
    }
}
