package utcapitole.miage.bloop.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import utcapitole.miage.bloop.service.AuthService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerStandaloneTest {

    private MockMvc setup(AuthController controller) {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("");
        viewResolver.setSuffix(".html");
        return MockMvcBuilders.standaloneSetup(controller)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void shouldShowLoginPage() throws Exception {
        AuthController controller = new AuthController(Mockito.mock(AuthService.class));
        setup(controller)
                .perform(get("/auth/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void shouldShowRegisterPage() throws Exception {
        AuthController controller = new AuthController(Mockito.mock(AuthService.class));
        setup(controller)
                .perform(get("/auth/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("inscription"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void shouldRegisterUser() throws Exception {
        // 1) Mock du service
        AuthService authSvc = Mockito.mock(AuthService.class);
        when(authSvc.enregistrerUtilisateur(any(), any(), any()))
                .thenReturn("accueil");

        // 2) Instanciation du controller
        AuthController controller = new AuthController(authSvc);

        // 3) Setup de MockMvc en standalone (sans springSecurity())
        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setViewResolvers(new InternalResourceViewResolver("", ".html"))
                .build();

        // 4) Crée un MockMultipartFile vide nommé "avatarFile"
        MockMultipartFile avatar = new MockMultipartFile(
                "avatarFile",
                "avatar.png",
                "image/png",
                new byte[0]
        );

        // 5) Monte la vraie requête multipart (CSRF facultatif ici)
        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/auth/register_user")
                        .file(avatar)
                        .param("nomUser",    "Dupont")
                        .param("prenomUser", "Jean")
                        .param("emailUser",  "jean@ut-capitole.fr")
                        .param("mdpUser",    "password")
                        .param("pseudoUser", "jdupont")
                        .param("telUser",    "0123456789")
                        .with(csrf())                // optionnel sans springSecurity()
                )
                .andExpect(status().isOk())
                .andExpect(view().name("accueil"));
    }


}