package utcapitole.miage.bloop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.RecommendationService;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RecommendationControllerTest {

    @Mock
    private RecommendationService recommendationService;

    @InjectMocks
    private RecommendationController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        InternalResourceViewResolver vr = new InternalResourceViewResolver();
        vr.setPrefix("");
        vr.setSuffix(".html");
        // enable @AuthenticationPrincipal resolution
        HandlerMethodArgumentResolver authResolver =
                new AuthenticationPrincipalArgumentResolver();
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setViewResolvers(vr)
                .setCustomArgumentResolvers(authResolver)
                .build();
    }

    @Test
    void testAfficherRecommandations() throws Exception {
        Utilisateur u = new Utilisateur();
        // note : pas besoin de setIdUser()
        List<Utilisateur> recs = List.of(new Utilisateur(), new Utilisateur());
        when(recommendationService.recommendFriends(anyLong()))
                .thenReturn(recs);

        // pas besoin de jouer avec SecurityContextHolder ici
        TestingAuthenticationToken auth =
                new TestingAuthenticationToken(u, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(get("/recommandations"))
                .andExpect(status().isOk())
                .andExpect(view().name("recommandations"))
                .andExpect(model().attribute("recommandations", recs));
    }
}
