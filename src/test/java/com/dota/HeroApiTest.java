package com.dota;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class HeroApiTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void shouldReturnHeroCatalog() throws Exception {
        mvc.perform(get("/api/heroes"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(
                        "application/json"
                ))
                .andExpect(jsonPath("$", hasSize(30)))
                .andExpect(jsonPath("$[0].id").isString())
                .andExpect(jsonPath("$[0].name").isString())
                .andExpect(jsonPath("$[0].position").isNumber())
                .andExpect(jsonPath("$[0].lanePower").isNumber())
                .andExpect(jsonPath("$[0].tempo").isNumber())
                .andExpect(jsonPath("$[0].greed").isNumber())
                .andExpect(jsonPath("$[0].scaling").isNumber())
                .andExpect(jsonPath("$[0].teamfight").isNumber())
                .andExpect(jsonPath("$[0].push").isNumber());
    }
}