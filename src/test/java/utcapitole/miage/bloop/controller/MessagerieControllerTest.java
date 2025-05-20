package utcapitole.miage.bloop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MessagerieControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        // Résout les vues "messagerie" en "messagerie.html" sans boucle
        InternalResourceViewResolver vr = new InternalResourceViewResolver();
        vr.setPrefix("");
        vr.setSuffix(".html");

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new MessagerieController())
                .setViewResolvers(vr)
                .build();
    }

    @Test
    void shouldAfficherMessagerie() throws Exception {
        mockMvc.perform(get("/messagerie"))
                .andExpect(status().isOk())
                .andExpect(view().name("messagerie"));
    }
}
