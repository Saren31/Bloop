package utcapitole.miage.bloop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessagerieController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class MessagerieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testAfficherMessagerie() throws Exception {
        mockMvc.perform(get("/messagerie"))
                .andExpect(status().isOk())
                .andExpect(view().name("messagerie"));
    }
}