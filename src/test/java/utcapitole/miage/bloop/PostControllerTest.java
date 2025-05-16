package utcapitole.miage.bloop;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import utcapitole.miage.bloop.controller.PostController;
import utcapitole.miage.bloop.service.PostService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    @Test
    void testCreerPostAvecTexteEtImage() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "imageFile",
                "image.jpg",
                "image/jpeg",
                "image data".getBytes()
        );

        mockMvc.perform(multipart("/post/creer")
                        .file(image)
                        .param("textePost", "Un message test"))
                .andExpect(status().is3xxRedirection());

        Mockito.verify(postService, Mockito.times(1)).creerPost(Mockito.any());
    }
}

