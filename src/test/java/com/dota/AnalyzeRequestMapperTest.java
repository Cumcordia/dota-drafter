package com.dota;

import static org.junit.jupiter.api.Assertions.*;

import com.dota.controllers.AnalyzeRequestMapper;
import com.dota.model.AnalyzeRequest;
import com.dota.model.AnalyzeRequest.HeroPick;
import com.dota.model.MatchDraft;
import java.util.List;
import org.junit.jupiter.api.Test;

class AnalyzeRequestMapperTest {
    private final AnalyzeRequestMapper mapper = new AnalyzeRequestMapper();

    private static HeroPick p(String id, int pos, String lane) { return new HeroPick(id, pos, lane); }

    private static List<HeroPick> radiant(String carryLane) {
        return List.of(p("juggernaut", 1, carryLane), p("puck", 2, "MID"), p("axe", 3, "TOP"),
                p("earthshaker", 4, "TOP"), p("crystal-maiden", 5, "BOTTOM"));
    }
    private static List<HeroPick> dire(String carryId) {
        return List.of(p(carryId, 1, "TOP"), p("invoker", 2, "MID"), p("mars", 3, "BOTTOM"),
                p("tusk", 4, "BOTTOM"), p("lion", 5, "TOP"));
    }

    @Test
    void validRequest_buildsMatchDraft() {
        MatchDraft d = mapper.apply(new AnalyzeRequest(radiant("BOTTOM"), dire("phantom-assassin")));
        assertEquals(5, d.radiant().size());
        assertEquals(5, d.dire().size());
    }

    @Test
    void hyphenAndUnderscoreSlugs_bothAccepted() {
        for (String id : List.of("anti-mage", "anti_mage")) {
            MatchDraft d = mapper.apply(new AnalyzeRequest(radiant("BOTTOM"), dire(id)));
            assertEquals("anti_mage", d.dire().get(0).id());
        }
    }

    @Test
    void wrongLane_rejected() {
        assertThrows(IllegalArgumentException.class,
                () -> mapper.apply(new AnalyzeRequest(radiant("TOP"), dire("phantom-assassin"))));
    }

    @Test
    void heroOnWrongPosition_rejected() {
        List<HeroPick> bad = List.of(p("axe", 1, "BOTTOM"), p("puck", 2, "MID"), p("axe", 3, "TOP"),
                p("earthshaker", 4, "TOP"), p("crystal-maiden", 5, "BOTTOM"));
        assertThrows(IllegalArgumentException.class,
                () -> mapper.apply(new AnalyzeRequest(bad, dire("phantom-assassin"))));
    }

    @Test
    void sameHeroInBothTeams_rejected() {
        assertThrows(IllegalArgumentException.class,
                () -> mapper.apply(new AnalyzeRequest(radiant("BOTTOM"), dire("juggernaut"))));
    }

    @Test
    void incompleteTeam_rejected() {
        assertThrows(IllegalArgumentException.class,
                () -> mapper.apply(new AnalyzeRequest(List.of(), dire("phantom-assassin"))));
    }
}