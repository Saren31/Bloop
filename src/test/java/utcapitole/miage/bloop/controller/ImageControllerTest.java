package utcapitole.miage.bloop.controller;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.UtilisateurService;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ImageControllerTest {

    @Mock
    private UtilisateurService utilisateurService;

    @InjectMocks
    private ImageController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        // 1) On enregistre le resolver pour @AuthenticationPrincipal Utilisateur
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setCustomArgumentResolvers(new AuthenticationPrincipalArgumentResolver())
                .build();
    }

    @Test
    void getAvatar_returnsImageBytes_whenPresent() throws Exception {
        byte[] avatar = {10, 20, 30};
        Utilisateur u = new Utilisateur();
        u.setAvatarUser(avatar);
        when(utilisateurService.getUtilisateurById(42L)).thenReturn(u);

        mockMvc.perform(get("/avatar/42"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(avatar));

        verify(utilisateurService).getUtilisateurById(42L);
    }

    @Test
    void getAvatar_returns404_whenUserNotFound() throws Exception {
        when(utilisateurService.getUtilisateurById(99L)).thenReturn(null);

        mockMvc.perform(get("/avatar/99"))
                .andExpect(status().isNotFound());

        verify(utilisateurService).getUtilisateurById(99L);
    }

    @Test
    void getAvatar_returns404_whenNoAvatarSet() throws Exception {
        Utilisateur u = new Utilisateur();
        u.setAvatarUser(null);
        when(utilisateurService.getUtilisateurById(7L)).thenReturn(u);

        mockMvc.perform(get("/avatar/7"))
                .andExpect(status().isNotFound());

        verify(utilisateurService).getUtilisateurById(7L);
    }

    //
    // POST /avatar/upload
    //

    @Test
    void uploadAvatar_savesAndRedirects_whenFileNonEmpty() throws Exception {
        byte[] data = {1, 2, 3};
        MockMultipartFile file = new MockMultipartFile(
                "avatar", "avatar.png", "image/png", data
        );

        // 2) On prépare notre Utilisateur et on le place dans le SecurityContext
        Utilisateur u = new Utilisateur();
        TestingAuthenticationToken auth = new TestingAuthenticationToken(u, null);
        org.springframework.security.core.context.SecurityContextHolder
                .getContext().setAuthentication(auth);

        mockMvc.perform(multipart("/avatar/upload")
                        .file(file)
                        .principal(auth)  // facultatif, mais explicite
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil/voirProfil"));

        // Vérifie bien qu'on a mis à jour l'entité et qu'on a appelé save(u)
        verify(utilisateurService).save(u);
        assertArrayEquals(data, u.getAvatarUser());
    }

    @Test
    void uploadAvatar_ignoresAndRedirects_whenFileEmpty() throws Exception {
        MockMultipartFile empty = new MockMultipartFile(
                "avatar", "empty.jpg", "image/jpeg", new byte[0]
        );

        // on s'assure qu'il n'y a PAS d'Authentication dans le contexte
        org.springframework.security.core.context.SecurityContextHolder.clearContext();

        mockMvc.perform(multipart("/avatar/upload")
                        .file(empty)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil/voirProfil"));

        // jamais de sauvegarde
        verify(utilisateurService, never()).save(any());
    }

    @Test
    void uploadAvatar_throwsException_whenSaveFails() {
        MockMultipartFile file = new MockMultipartFile(
                "avatar","a.jpg","image/jpeg",new byte[]{9,9}
        );
        Utilisateur u = new Utilisateur();
        TestingAuthenticationToken auth = new TestingAuthenticationToken(u,null);
        SecurityContextHolder.getContext().setAuthentication(auth);
        doThrow(new RuntimeException("boom")).when(utilisateurService).save(u);

        // on s'attend à ce que l'exception remonte
        Assertions.assertThrows(ServletException.class, () -> {
            mockMvc.perform(multipart("/avatar/upload").file(file).principal(auth));
        });
    }
}
