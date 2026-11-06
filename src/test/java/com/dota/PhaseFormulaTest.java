package com.dota;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import com.dota.analysis.AnalysisFacade;
import com.dota.analysis.LategameStrategy;
import com.dota.analysis.MidgameStrategy;
import com.dota.analysis.PhaseAnalysisStrategy;
import com.dota.analysis.PhaseInput;
import com.dota.analysis.ScoreComponent;
import com.dota.analysis.TeamfightModifier;
import com.dota.analysis.TempoGreedBase;

import com.dota.model.*;

class PhaseFormulaTest {

    // TODO изменить после мержа
    private static Hero hero(double tempo, double greed, double scaling, double tf, double push) {
        return new Hero("test", "Test", 1, 5, tempo, greed, scaling, tf, push); // lanePower = 5
    }
    private static MatchDraft draft(double direScaling) {
        List<Hero> r = Collections.nCopies(5, hero(7, 4, 5, 6, 5));
        List<Hero> d = Collections.nCopies(5, hero(5, 6, direScaling, 4, 3));
        return new MatchDraft(r, d);
    }
    private static MatchDraft swapped(MatchDraft m) { return new MatchDraft(m.dire(), m.radiant()); }
    private static LaningResult laning(double overall) {
        LaneResult lane = new LaneResult(overall, List.of());
        return new LaningResult(lane, lane, lane);
    }

    private final PhaseAnalysisStrategy mid = new MidgameStrategy();
    private final PhaseAnalysisStrategy late = new LategameStrategy();

    @Test
    void midgame_fixedExample() {
        PhaseResult r = mid.analyze(new PhaseInput(draft(7.5), 2.0, 0));
        assertEquals(4.9, r.diff(), 1e-9);
        assertEquals("Strong Radiant Advantage", r.label());
    }

    @Test
    void lategame_fixedExample() {
        PhaseResult r = late.analyze(new PhaseInput(draft(7.5), 2.0, 4.9));
        assertEquals(-1.02, r.diff(), 1e-9);
        assertEquals("Slight Dire Advantage", r.label());
    }

    @Test
    void laningDiff_entersMidgameWithCoefficient035() {
        double with = mid.analyze(new PhaseInput(draft(7.5), 2.0, 0)).diff();
        double without = mid.analyze(new PhaseInput(draft(7.5), 0.0, 0)).diff();
        assertEquals(0.35 * 2.0, with - without, 1e-9);
    }

    @Test
    void scalingChange_movesLateOnly() {
        PhaseInput a = new PhaseInput(draft(7.5), 2.0, 4.9);
        PhaseInput b = new PhaseInput(draft(6.5), 2.0, 4.9);
        assertEquals(1.0, late.analyze(b).diff() - late.analyze(a).diff(), 1e-9);
        assertEquals(mid.analyze(a).diff(), mid.analyze(b).diff(), 1e-9); // mid не зависит от scaling
    }

    @Test
    void swappingTeams_flipsSigns() {
        MatchDraft s = swapped(draft(7.5));
        assertEquals(-4.9, mid.analyze(new PhaseInput(s, -2.0, 0)).diff(), 1e-9);
        assertEquals(1.02, late.analyze(new PhaseInput(s, -2.0, -4.9)).diff(), 1e-9);
    }

    @Test
    void decorator_addsOnlyItsOwnTerm() {
        PhaseInput in = new PhaseInput(draft(7.5), 2.0, 0);
        ScoreComponent base = new TempoGreedBase();
        ScoreComponent with = new TeamfightModifier(base, 0.4);
        assertEquals(0.4 * 2.0, with.value(in) - base.value(in), 1e-9); // diff teamfight = 2
    }

    @Test
    void labels_boundaries() {
        assertEquals("Even", PhaseResult.of(0.49).label());
        assertEquals("Slight Radiant Advantage", PhaseResult.of(0.5).label());
        assertEquals("Slight Dire Advantage", PhaseResult.of(-0.7).label());
        assertEquals("Strong Radiant Advantage", PhaseResult.of(1.5).label());
        assertEquals("Strong Dire Advantage", PhaseResult.of(-1.5).label());
    }

    @Test
    void facade_fullScenario() {
        AnalysisFacade facade = new AnalysisFacade(d -> laning(2.0), mid, late);
        AnalysisResponse res = facade.analyze(draft(7.5));

        assertEquals(4.9, res.midgame().diff(), 1e-9);
        assertEquals(-1.02, res.lategame().diff(), 1e-9);
        assertEquals(1.5, res.thresholdX());
        assertEquals(3, res.summary().size());
        assertTrue(res.summary().contains("Radiant has better tempo"));
        assertTrue(res.summary().contains("Dire has better scaling"));
    }
}