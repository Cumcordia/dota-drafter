package com.dota.controllers;

import com.dota.model.AnalyzeRequest;
import com.dota.model.AnalyzeRequest.HeroPick;
import com.dota.model.MatchDraft;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnalyzeRequestMapperTest {

    private final AnalyzeRequestMapper mapper = new AnalyzeRequestMapper();

    @Test
    void shouldBuildValidMatchDraft() {
        MatchDraft draft = mapper.apply(
                new AnalyzeRequest(radiant(), dire())
        );

        assertEquals(5, draft.radiant().size());
        assertEquals(5, draft.dire().size());

        assertEquals("juggernaut", draft.radiant().get(0).id());
        assertEquals("phantom_assassin", draft.dire().get(0).id());
    }

    @Test
    void shouldAcceptHyphenAndUnderscoreIds() {
        for (String id : List.of("anti-mage", "anti_mage")) {
            List<HeroPick> picks = new ArrayList<>(radiant());
            picks.set(0, new HeroPick(id, 1, "BOTTOM"));

            MatchDraft draft = mapper.apply(
                    new AnalyzeRequest(picks, dire())
            );

            assertEquals("anti_mage", draft.radiant().get(0).id());
        }
    }

    @Test
    void shouldRejectSameHeroInBothTeams() {
        List<HeroPick> picks = new ArrayList<>(dire());
        picks.set(0, new HeroPick("juggernaut", 1, "TOP"));

        assertThrows(
                IllegalArgumentException.class,
                () -> mapper.apply(new AnalyzeRequest(radiant(), picks))
        );
    }

    @Test
    void shouldRejectDuplicatePosition() {
        List<HeroPick> picks = new ArrayList<>(radiant());
        picks.set(1, new HeroPick("luna", 1, "BOTTOM"));

        assertThrows(
                IllegalArgumentException.class,
                () -> mapper.apply(new AnalyzeRequest(picks, dire()))
        );
    }

    @Test
    void shouldRejectWrongHeroPosition() {
        List<HeroPick> picks = new ArrayList<>(radiant());
        picks.set(0, new HeroPick("axe", 1, "BOTTOM"));

        assertThrows(
                IllegalArgumentException.class,
                () -> mapper.apply(new AnalyzeRequest(picks, dire()))
        );
    }

    @Test
    void shouldRejectWrongLane() {
        List<HeroPick> picks = new ArrayList<>(radiant());
        picks.set(0, new HeroPick("juggernaut", 1, "TOP"));

        assertThrows(
                IllegalArgumentException.class,
                () -> mapper.apply(new AnalyzeRequest(picks, dire()))
        );
    }

    @Test
    void shouldRejectInvalidPosition() {
        for (int position : List.of(0, 6)) {
            List<HeroPick> picks = new ArrayList<>(radiant());
            picks.set(0, new HeroPick("juggernaut", position, "BOTTOM"));

            assertThrows(
                    IllegalArgumentException.class,
                    () -> mapper.apply(new AnalyzeRequest(picks, dire()))
            );
        }
    }

    @Test
    void shouldRejectIncompleteTeam() {
        List<HeroPick> picks = new ArrayList<>(radiant());
        picks.remove(0);

        assertThrows(
                IllegalArgumentException.class,
                () -> mapper.apply(new AnalyzeRequest(picks, dire()))
        );
    }

    @Test
    void shouldRejectTeamWithTooManyHeroes() {
        List<HeroPick> picks = new ArrayList<>(radiant());
        picks.add(new HeroPick("luna", 1, "BOTTOM"));

        assertThrows(
                IllegalArgumentException.class,
                () -> mapper.apply(new AnalyzeRequest(picks, dire()))
        );
    }

    @Test
    void shouldRejectMissingTeam() {
        assertThrows(
                IllegalArgumentException.class,
                () -> mapper.apply(new AnalyzeRequest(null, dire()))
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> mapper.apply(new AnalyzeRequest(radiant(), null))
        );
    }

    @Test
    void shouldRejectNullPick() {
        List<HeroPick> picks = new ArrayList<>(radiant());
        picks.set(0, null);

        assertThrows(
                IllegalArgumentException.class,
                () -> mapper.apply(new AnalyzeRequest(picks, dire()))
        );
    }

    @Test
    void shouldRejectEmptyHeroId() {
        for (String id : List.of("", " ")) {
            List<HeroPick> picks = new ArrayList<>(radiant());
            picks.set(0, new HeroPick(id, 1, "BOTTOM"));

            assertThrows(
                    IllegalArgumentException.class,
                    () -> mapper.apply(new AnalyzeRequest(picks, dire()))
            );
        }
    }

    @Test
    void shouldRejectNullHeroId() {
        List<HeroPick> picks = new ArrayList<>(radiant());
        picks.set(0, new HeroPick(null, 1, "BOTTOM"));

        assertThrows(
                IllegalArgumentException.class,
                () -> mapper.apply(new AnalyzeRequest(picks, dire()))
        );
    }

    @Test
    void shouldRejectUnknownHero() {
        List<HeroPick> picks = new ArrayList<>(radiant());
        picks.set(0, new HeroPick("unknown_hero", 1, "BOTTOM"));

        assertThrows(
                IllegalArgumentException.class,
                () -> mapper.apply(new AnalyzeRequest(picks, dire()))
        );
    }

    @Test
    void shouldRejectNullLane() {
        List<HeroPick> picks = new ArrayList<>(radiant());
        picks.set(0, new HeroPick("juggernaut", 1, null));

        assertThrows(
                IllegalArgumentException.class,
                () -> mapper.apply(new AnalyzeRequest(picks, dire()))
        );
    }

    @Test
    void shouldRejectNullRequest() {
        assertThrows(
                IllegalArgumentException.class,
                () -> mapper.apply(null)
        );
    }

    private List<HeroPick> radiant() {
        return List.of(
                new HeroPick("juggernaut", 1, "BOTTOM"),
                new HeroPick("puck", 2, "MID"),
                new HeroPick("axe", 3, "TOP"),
                new HeroPick("earthshaker", 4, "TOP"),
                new HeroPick("crystal-maiden", 5, "BOTTOM")
        );
    }

    private List<HeroPick> dire() {
        return List.of(
                new HeroPick("phantom-assassin", 1, "TOP"),
                new HeroPick("invoker", 2, "MID"),
                new HeroPick("mars", 3, "BOTTOM"),
                new HeroPick("tusk", 4, "BOTTOM"),
                new HeroPick("lion", 5, "TOP")
        );
    }
}