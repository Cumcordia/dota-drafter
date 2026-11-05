package com.dota;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AnalyzeApiTest {
    @Autowired MockMvc mvc;

    static final String DEMO = """
        {"radiant":[
          {"heroId":"juggernaut","position":1,"lane":"BOTTOM"},
          {"heroId":"puck","position":2,"lane":"MID"},
          {"heroId":"axe","position":3,"lane":"TOP"},
          {"heroId":"earthshaker","position":4,"lane":"TOP"},
          {"heroId":"crystal-maiden","position":5,"lane":"BOTTOM"}],
         "dire":[
          {"heroId":"phantom-assassin","position":1,"lane":"TOP"},
          {"heroId":"invoker","position":2,"lane":"MID"},
          {"heroId":"mars","position":3,"lane":"BOTTOM"},
          {"heroId":"tusk","position":4,"lane":"BOTTOM"},
          {"heroId":"lion","position":5,"lane":"TOP"}]}
        """;

    @Test
    void demoRequest_returnsAllPhases() throws Exception {
        mvc.perform(post("/api/analyze").contentType(MediaType.APPLICATION_JSON).content(DEMO))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.laning.top.diff").exists())
           .andExpect(jsonPath("$.laning.overallDiff").exists())
           .andExpect(jsonPath("$.midgame.label").exists())
           .andExpect(jsonPath("$.lategame.diff").exists())
           .andExpect(jsonPath("$.thresholdX").value(1.5));
    }

    @Test
    void incompleteDraft_returns400() throws Exception {
        // предполагает, что DraftBuilder A бросает IllegalArgumentException
        mvc.perform(post("/api/analyze").contentType(MediaType.APPLICATION_JSON)
                .content("{\"radiant\":[],\"dire\":[]}"))
           .andExpect(status().isBadRequest());
    }
}