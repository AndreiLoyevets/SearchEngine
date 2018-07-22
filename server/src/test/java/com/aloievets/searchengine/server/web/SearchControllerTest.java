package com.aloievets.searchengine.server.web;


import com.aloievets.searchengine.server.ServerRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerRunner.class)
@WebAppConfiguration
public class SearchControllerTest {

    private static final String CONTROLLER_ROOT = "/searchengine/";

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void putNewDocument() throws Exception {
        String key = "shoppinglist";
        String document = "milk salad tomato";

        mockMvc.perform(put(CONTROLLER_ROOT + key)
                .content(document));

        mockMvc.perform(get(CONTROLLER_ROOT + key))
                .andExpect(status().isOk())
                .andExpect(content().string(document));
    }

    @Test
    public void searchPhrase() throws Exception {
        String key = "shoppinglist";
        String document = "milk salad tomato";
        String searchPhrase = "salad tomato";

        mockMvc.perform(put(CONTROLLER_ROOT + key)
                .content(document));

        mockMvc.perform(get(CONTROLLER_ROOT)
                .param("searchPhrase", searchPhrase))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]", is(key)));
    }

    @Test
    public void searchPhraseMultipleDocuments() throws Exception {
        String key1 = "shoppinglist";
        String document1 = "milk salad tomato";
        String key2 = "todolist";
        String document2 = "gym salad tomato";
        String key3 = "poem";
        String document3 = "my mistress eyes are nothing like the Sun";

        String searchPhrase = "salad tomato";

        mockMvc.perform(put(CONTROLLER_ROOT + key1)
                .content(document1));
        mockMvc.perform(put(CONTROLLER_ROOT + key2)
                .content(document2));
        mockMvc.perform(put(CONTROLLER_ROOT + key3)
                .content(document3));

        mockMvc.perform(get(CONTROLLER_ROOT)
                .param("searchPhrase", searchPhrase))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$", hasItems(key1, key2)));
    }
}