package utcapitole.miage.bloop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import utcapitole.miage.bloop.service.AuthService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@ActiveProfiles("test")
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Test
    @WithMockUser // Simule un utilisateur authentifié
    void shouldShowLoginPage() throws Exception {
        mockMvc.perform(get("/auth/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    @WithMockUser // Simule un utilisateur authentifié
    void shouldShowRegisterPage() throws Exception {
        mockMvc.perform(get("/auth/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("inscription"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @WithMockUser // Simule un utilisateur authentifié
    void shouldRegisterUser() throws Exception {
        when(authService.enregistrerUtilisateur(any(), any(), any())).thenReturn("accueil");

        mockMvc.perform(post("/auth/register_user")
                        .with(csrf()) // Ajout du token CSRF
                        .param("nomUser", "Dupont")
                        .param("prenomUser", "Jean")
                        .param("emailUser", "jean@ut-capitole.fr")
                        .param("mdpUser", "password")
                        .param("pseudoUser", "jdupont")
                        .param("telUser", "0123456789"))
                .andExpect(status().isOk())
                .andExpect(view().name("accueil"));
    }

    //Impossible de tester la déconnexion de manière automatisée
}