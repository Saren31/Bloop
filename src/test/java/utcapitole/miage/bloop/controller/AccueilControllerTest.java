package utcapitole.miage.bloop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Tests unitaires du controller AccueilController en mode standalone (sans Spring Context).
 */
class AccueilControllerTest {

    private MockMvc setup(AccueilController controller) {
        // Configure un ViewResolver pour éviter le dispatch cyclique
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("");
        viewResolver.setSuffix(".html");
        return MockMvcBuilders.standaloneSetup(controller)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void shouldShowAccueilPage() throws Exception {
        AccueilController controller = new AccueilController();
        setup(controller)
                .perform(get("/accueil"))
                .andExpect(status().isOk())
                .andExpect(view().name("accueil"));
    }

    @Test
    void shouldShowIndexPage() throws Exception {
        AccueilController controller = new AccueilController();
        setup(controller)
                .perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("accueil"));
    }
}
