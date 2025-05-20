package utcapitole.miage.bloop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import utcapitole.miage.bloop.dto.PostDTO;
import utcapitole.miage.bloop.service.PostService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class GroupeRestControllerTest {

    @Mock
    private PostService postService;

    @InjectMocks
    private GroupeRestController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        // Monte le contrôleur REST sans contexte Spring complet
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void shouldReturnPostsByGroupe() throws Exception {
        // Prépare le mock du service
        when(postService.getPostsByGroupe(1L))
                .thenReturn(List.of(new PostDTO()));

        // Exécute la requête GET et vérifie le statut 200 OK
        mockMvc.perform(get("/groupes/1/posts"))
                .andExpect(status().isOk());
    }
}
