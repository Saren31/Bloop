package utcapitole.miage.bloop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import utcapitole.miage.bloop.dto.PostDTO;
import utcapitole.miage.bloop.service.PostService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GroupeRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class GroupeRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    @Test
    void testGetPostsByGroupe() throws Exception {
        when(postService.getPostsByGroupe(1L)).thenReturn(List.of(new PostDTO()));

        mockMvc.perform(get("/groupes/1/posts"))
                .andExpect(status().isOk());
    }
}