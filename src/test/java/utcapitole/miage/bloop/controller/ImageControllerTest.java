package utcapitole.miage.bloop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.UtilisateurService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void testGetAvatar_Success() throws Exception {
        byte[] avatar = {1, 2, 3, 4};
        Utilisateur u = new Utilisateur();
        u.setAvatarUser(avatar);
        when(utilisateurService.getUtilisateurById(5L)).thenReturn(u);

        mockMvc.perform(get("/avatar/5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(avatar));
    }

    @Test
    void testGetAvatar_NotFound_NoUser() throws Exception {
        when(utilisateurService.getUtilisateurById(99L)).thenReturn(null);

        mockMvc.perform(get("/avatar/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAvatar_NotFound_NoAvatar() throws Exception {
        Utilisateur u = new Utilisateur();
        u.setAvatarUser(null);
        when(utilisateurService.getUtilisateurById(7L)).thenReturn(u);

        mockMvc.perform(get("/avatar/7"))
                .andExpect(status().isNotFound());
    }
}
