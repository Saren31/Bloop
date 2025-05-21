// src/test/java/utcapitole/miage/bloop/controller/RecommendationRestControllerTest.java
package utcapitole.miage.bloop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import utcapitole.miage.bloop.model.entity.Utilisateur;
import utcapitole.miage.bloop.service.RecommendationService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RecommendationRestControllerTest {

    @Mock
    private RecommendationService recommendationService;

    @InjectMocks
    private RecommendationRestController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .build();
    }

    @Test
    void testGetRecommendations_Success() throws Exception {
        Utilisateur u1 = new Utilisateur(); u1.setIdUser(1L);
        Utilisateur u2 = new Utilisateur(); u2.setIdUser(2L);
        when(recommendationService.recommendFriends(5L))
                .thenReturn(List.of(u1, u2));

        mockMvc.perform(get("/recommendations/5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idUser").value(1))
                .andExpect(jsonPath("$[1].idUser").value(2));
    }
}
