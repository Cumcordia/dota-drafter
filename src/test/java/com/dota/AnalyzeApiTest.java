package com.dota;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.closeTo;

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
    void demoRequest_matchesHandCalculatedFixture() throws Exception {
        mvc.perform(post("/api/analyze").contentType(MediaType.APPLICATION_JSON).content(DEMO))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.laning.top.diff", closeTo(1.5, 1e-9)))
        .andExpect(jsonPath("$.laning.mid.diff", closeTo(1.0, 1e-9)))
        .andExpect(jsonPath("$.laning.bottom.diff", closeTo(0.5, 1e-9)))
        .andExpect(jsonPath("$.laning.overallDiff", closeTo(1.0, 1e-9)))
        .andExpect(jsonPath("$.midgame.diff", closeTo(1.59, 1e-9)))
        .andExpect(jsonPath("$.midgame.label").value("Strong Radiant Advantage"))
        .andExpect(jsonPath("$.lategame.diff", closeTo(0.218, 1e-9)))
        .andExpect(jsonPath("$.lategame.label").value("Even"))
        .andExpect(jsonPath("$.thresholdX").value(1.5));
    }

    @Test
    void incompleteDraft_returns400() throws Exception {
        mvc.perform(post("/api/analyze").contentType(MediaType.APPLICATION_JSON)
                .content("{\"radiant\":[],\"dire\":[]}"))
           .andExpect(status().isBadRequest());
    }
}