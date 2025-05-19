package utcapitole.miage.bloop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import utcapitole.miage.bloop.dto.UtilisateurDTO;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.RelationService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RelationController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class RelationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RelationService relationService;

    @Test
    void testEnvoyerDemandeAmitie_Succes() throws Exception {
        when(relationService.envoyerDemandeAmitie(1L, 2L)).thenReturn("Demande envoyée avec succès");

        mockMvc.perform(post("/relations/demande")
                        .param("idEnvoyeur", "1")
                        .param("idReceveur", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Demande envoyée avec succès"));
    }

    @Test
    void testEnvoyerDemandeAmitie_Echec() throws Exception {
        when(relationService.envoyerDemandeAmitie(1L, 2L)).thenReturn("Erreur");

        mockMvc.perform(post("/relations/demande")
                        .param("idEnvoyeur", "1")
                        .param("idReceveur", "2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Erreur"));
    }

    @Test
    void testAccepterDemandeAmitie_Succes() throws Exception {
        when(relationService.gererDemandeAmitie(2L, 1L, true)).thenReturn("Demande d'amitié acceptée.");

        mockMvc.perform(post("/relations/accepter")
                        .param("idReceveur", "2")
                        .param("idEnvoyeur", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Demande d'amitié acceptée."));
    }

    @Test
    void testAccepterDemandeAmitie_Echec() throws Exception {
        when(relationService.gererDemandeAmitie(2L, 1L, true)).thenReturn("Erreur");

        mockMvc.perform(post("/relations/accepter")
                        .param("idReceveur", "2")
                        .param("idEnvoyeur", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Erreur"));
    }

    @Test
    void testRefuserDemandeAmitie_Succes() throws Exception {
        when(relationService.gererDemandeAmitie(2L, 1L, false)).thenReturn("Demande d'amitié refusée.");

        mockMvc.perform(post("/relations/refuser")
                        .param("idReceveur", "2")
                        .param("idEnvoyeur", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Demande d'amitié refusée."));
    }

    @Test
    void testRefuserDemandeAmitie_Echec() throws Exception {
        when(relationService.gererDemandeAmitie(2L, 1L, false)).thenReturn("Erreur");

        mockMvc.perform(post("/relations/refuser")
                        .param("idReceveur", "2")
                        .param("idEnvoyeur", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Erreur"));
    }

    @Test
    void testVoirListeAmis_Succes() throws Exception {
        // Créer un DTO au lieu d'une entité
        UtilisateurDTO amiDTO = new UtilisateurDTO();
        amiDTO.setIdUser(3L);
        amiDTO.setNomUser("Ami");
        amiDTO.setPrenomUser("Prénom");
        amiDTO.setPseudoUser("pseudo");
        amiDTO.setEmailUser("ami@ut-capitole.fr");

        // Mock du service pour retourner une liste de DTOs
        when(relationService.getListeAmis(1L)).thenReturn(List.of(amiDTO));

        // Test de l'endpoint
        mockMvc.perform(get("/relations/amis")
                        .param("idUser", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idUser").value(3))
                .andExpect(jsonPath("$[0].nomUser").value("Ami"))
                .andExpect(jsonPath("$[0].prenomUser").value("Prénom"))
                .andExpect(jsonPath("$[0].pseudoUser").value("pseudo"))
                .andExpect(jsonPath("$[0].emailUser").value("ami@ut-capitole.fr"));
    }

    @Test
    void testSupprimerAmi_Succes() throws Exception {
        when(relationService.supprimerAmi(1L, 2L)).thenReturn("Suppression avec succès");

        mockMvc.perform(delete("/relations/supprimer")
                        .param("idUser", "1")
                        .param("idAmi", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Suppression avec succès"));
    }

    @Test
    void testSupprimerAmi_Echec() throws Exception {
        when(relationService.supprimerAmi(1L, 2L)).thenReturn("Erreur");

        mockMvc.perform(delete("/relations/supprimer")
                        .param("idUser", "1")
                        .param("idAmi", "2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Erreur"));
    }
}