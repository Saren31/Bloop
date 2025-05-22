package utcapitole.miage.bloop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import utcapitole.miage.bloop.service.EvenementService;
import utcapitole.miage.bloop.service.GroupeService;
import utcapitole.miage.bloop.service.PostService;
import utcapitole.miage.bloop.service.ReactionService;
import utcapitole.miage.bloop.service.UtilisateurService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class AccueilControllerTest {

    private MockMvc mockMvc;
    private EvenementService evenementService;
    private GroupeService groupeService;
    private PostService postService;
    private UtilisateurService utilisateurService;
    private ReactionService reactionService;

    @BeforeEach
    void setUp() {
        evenementService = Mockito.mock(EvenementService.class);
        groupeService = Mockito.mock(GroupeService.class);
        postService = Mockito.mock(PostService.class);
        utilisateurService = Mockito.mock(UtilisateurService.class);
        reactionService = Mockito.mock(ReactionService.class);

        AccueilController controller = new AccueilController(
                evenementService,
                groupeService,
                postService,
                utilisateurService,
                reactionService
        );

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void shouldShowAccueilPage() throws Exception {
        mockMvc.perform(get("/accueil"))
                .andExpect(status().isOk())
                .andExpect(view().name("accueil"));
    }

    @Test
    void shouldShowIndexPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("accueil"));
    }
}