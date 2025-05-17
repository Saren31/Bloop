package utcapitole.miage.bloop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

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
        RequestPostProcessor csrfToken = request -> {
            CsrfToken token = new DefaultCsrfToken("X-CSRF-TOKEN", "_csrf", "dummy-csrf-token");
            request.setAttribute("_csrf", token);
            return request;
        };

        mockMvc.perform(get("/messagerie").with(csrfToken))
                .andExpect(status().isOk())
                .andExpect(view().name("messagerie"));
    }
}