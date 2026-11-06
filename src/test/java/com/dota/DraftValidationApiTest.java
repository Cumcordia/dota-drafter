package com.dota;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DraftValidationApiTest {

    @Autowired
    private MockMvc mvc;

    private static final String VALID_DRAFT = """
            {
              "radiant": [
                {"heroId":"juggernaut","position":1,"lane":"BOTTOM"},
                {"heroId":"puck","position":2,"lane":"MID"},
                {"heroId":"axe","position":3,"lane":"TOP"},
                {"heroId":"earthshaker","position":4,"lane":"TOP"},
                {"heroId":"crystal-maiden","position":5,"lane":"BOTTOM"}
              ],
              "dire": [
                {"heroId":"phantom-assassin","position":1,"lane":"TOP"},
                {"heroId":"invoker","position":2,"lane":"MID"},
                {"heroId":"mars","position":3,"lane":"BOTTOM"},
                {"heroId":"tusk","position":4,"lane":"BOTTOM"},
                {"heroId":"lion","position":5,"lane":"TOP"}
              ]
            }
            """;

    @Test
    void shouldAcceptValidDraft() throws Exception {
        mvc.perform(post("/api/draft/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_DRAFT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.errors", hasSize(0)));
    }

    @Test
    void shouldRejectIncompleteDraft() throws Exception {
        mvc.perform(post("/api/draft/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"radiant":[],"dire":[]}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.errors[0]").isString());
    }

    @Test
    void shouldRejectHeroPickedInBothTeams() throws Exception {
        String draft = VALID_DRAFT.replace(
                "phantom-assassin",
                "juggernaut"
        );

        mvc.perform(post("/api/draft/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(draft))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.errors[0]").value(
                        "Hero picked in both teams: juggernaut"
                ));
    }

    @Test
    void shouldRejectWrongLane() throws Exception {
        String draft = VALID_DRAFT.replace(
                "\"heroId\":\"juggernaut\",\"position\":1,\"lane\":\"BOTTOM\"",
                "\"heroId\":\"juggernaut\",\"position\":1,\"lane\":\"TOP\""
        );

        mvc.perform(post("/api/draft/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(draft))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.errors[0]").value(
                        "Radiant position 1 must be assigned to BOTTOM"
                ));
    }
}