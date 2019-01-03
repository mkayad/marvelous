package com.mkayad.codechallenge;


import com.mkayad.codechallenge.config.CacheConfiguration;
import com.mkayad.codechallenge.controllers.Characters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(Characters.class)
@WebAppConfiguration
@ContextConfiguration(classes = { CacheConfiguration.class,Application.class })
public class CharactersControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private Characters charactersController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testListOfCharacters() throws Exception {
        MvcResult result = mockMvc
                .perform(get("/characters").contentType(APPLICATION_XML))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc
                .perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("[")))
                .andExpect(content().string(containsString("]")));
    }

    @Test
    public void testSpecificCharacterId() throws Exception {
        MvcResult result = mockMvc
                .perform(get("/characters/1010906").contentType(APPLICATION_JSON))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value("1010906"));


    }

    @Test
    public void testCharacterPowers() throws Exception {
        MvcResult result = mockMvc
                .perform(get("/characters/1010906/powers").contentType(APPLICATION_JSON))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("durability")));
    }

    @Test
    public void testCharacterPowersTanslatedToFrench() throws Exception {
        MvcResult result = mockMvc
                .perform(get("/characters/1010906/powers?language=fr").contentType(APPLICATION_JSON))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("durabilité")));
    }

    @Test
    public void testUnvalidCharacterId() throws Exception {
        MvcResult result = mockMvc
                    .perform(get("/characters/-1111").contentType(APPLICATION_JSON))
                    .andExpect(request().asyncStarted())
                    .andReturn();

        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Error")));
    }
    @Test
    public void testUnvalidLanguageTranslation() throws Exception {
        MvcResult result = mockMvc
                    .perform(get("/characters/1010906/powers?language=un").contentType(APPLICATION_JSON))
                    .andExpect(request().asyncStarted())
                    .andReturn();

        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Unknown language")));
    }
}
