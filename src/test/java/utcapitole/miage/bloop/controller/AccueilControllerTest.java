package utcapitole.miage.bloop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ActiveProfiles("test")
@WebMvcTest(AccueilController.class)
@AutoConfigureMockMvc(addFilters = false)
class AccueilControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldShowAccueilPage() throws Exception {
        mockMvc.perform(get("/accueil"))
                .andExpect(status().isOk())
                .andExpect(view().name("accueil"));
    }
}
