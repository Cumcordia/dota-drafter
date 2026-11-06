package com.dota.laning;

import com.dota.draft.*;
import com.dota.model.Hero;
import com.dota.model.LaneResult;
import com.dota.model.LaningResult;
import com.dota.model.MatchDraft;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LaningAnalyzerTest {

    private static final double EPS = 1e-9;

    private final LaningAnalyzer analyzer = new LaningAnalyzer();

    @Test
    void shouldCalculateLaneDifference() {
        Hero radiant = hero("radiant_mid", 8);
        Hero dire = hero("dire_mid", 6);

        assertEquals(
                2.0,
                analyzer.calculateLanePower(radiant, dire),
                EPS
        );
    }

    @Test
    void duoBaseUsesAverageWithoutRules() {
        double result = analyzer.calculateDuoLane(
                hero("r1", 8),
                hero("r2", 6),
                hero("d1", 4),
                hero("d2", 6)
        );

        assertEquals(2.0, result, EPS);
    }

    @Test
    void shouldCalculateTopLane() {
        double result = analyzer.calculateDuoLane(
                hero("axe", 8),
                hero("tusk", 7),
                hero("juggernaut", 6),
                hero("lion", 5)
        );

        assertEquals(3.5, result, EPS);
    }

    @Test
    void shouldAnalyzeFullDraft() {
        MatchDraft draft = new MatchDraft(
                List.of(
                        new CarryFactory().createHero("juggernaut"),
                        new MidFactory().createHero("puck"),
                        new OfflaneFactory().createHero("axe"),
                        new SoftSupportFactory().createHero("tusk"),
                        new HardSupportFactory().createHero("lion")
                ),
                List.of(
                        new CarryFactory().createHero("luna"),
                        new MidFactory().createHero("invoker"),
                        new OfflaneFactory().createHero("mars"),
                        new SoftSupportFactory().createHero("rubick"),
                        new HardSupportFactory().createHero("dazzle")
                )
        );

        LaningResult result = analyzer.analyze(draft);

        assertEquals(1.5, result.top().diff(), EPS);
        assertEquals(1.0, result.mid().diff(), EPS);
        assertEquals(0.0, result.bottom().diff(), EPS);
        assertEquals(2.5 / 3.0, result.overallDiff(), EPS);

        assertTrue(result.top().reasons().stream()
                .anyMatch(reason -> reason.contains("Average lane power")));

        assertTrue(result.top().reasons().stream()
                .anyMatch(reason -> reason.contains("Radiant synergy: Axe + Tusk")));

        assertTrue(result.mid().reasons().stream()
                .anyMatch(reason -> reason.contains("Puck") && reason.contains("Invoker")));

        assertTrue(result.bottom().reasons().stream()
                .noneMatch(reason -> reason.contains("synergy")));
    }

    @Test
    void shouldCalculateLaneSynergy() {
        Hero axe = new OfflaneFactory().createHero("axe");
        Hero tusk = new SoftSupportFactory().createHero("tusk");

        assertEquals(1.5, analyzer.calculateSynergy(axe, tusk), EPS);
        assertEquals(1.5, analyzer.calculateSynergy(tusk, axe), EPS);
    }

    @Test
    void direSynergyIsSubtractedAfterAveraging() {
        double result = analyzer.calculateDuoLane(
                hero("r1", 6),
                hero("r2", 6),
                hero("juggernaut", 6),
                hero("crystal_maiden", 6)
        );

        assertEquals(-1.5, result, EPS);
    }

    @Test
    void shouldCalculateMatchupBonus() {
        Hero axe = new OfflaneFactory().createHero("axe");
        Hero pa = new CarryFactory().createHero("phantom assassin");

        assertEquals(1.0, analyzer.calculateMatchupBonus(axe, pa), EPS);
        assertEquals(-1.0, analyzer.calculateMatchupBonus(pa, axe), EPS);
    }

    @Test
    void shouldApplyMatchupBonus() {
        Hero axe = new OfflaneFactory().createHero("axe");
        Hero tusk = new SoftSupportFactory().createHero("tusk");
        Hero pa = new CarryFactory().createHero("phantom assassin");
        Hero lion = new HardSupportFactory().createHero("lion");

        double result = analyzer.calculateDuoLane(axe, tusk, pa, lion);

        assertEquals(4.0, result, EPS);
    }

    @Test
    void swappingDuoSidesFlipsSign() {
        Hero axe = new OfflaneFactory().createHero("axe");
        Hero tusk = new SoftSupportFactory().createHero("tusk");
        Hero pa = new CarryFactory().createHero("phantom assassin");
        Hero lion = new HardSupportFactory().createHero("lion");

        double original = analyzer.calculateDuoLane(axe, tusk, pa, lion);
        double swapped = analyzer.calculateDuoLane(pa, lion, axe, tusk);

        assertEquals(-original, swapped, EPS);
    }

    @Test
    void combinedCounterAdjustmentIsLimitedPerLane() {
        Hero axe = hero("axe", 5);
        Hero antiMage = hero("anti_mage", 5);
        Hero pa = hero("phantom_assassin", 5);
        Hero storm = hero("storm_spirit", 5);

        double original = analyzer.calculateDuoLane(
                axe, antiMage, pa, storm
        );

        double swapped = analyzer.calculateDuoLane(
                pa, storm, axe, antiMage
        );

        assertEquals(1.0, original, EPS);
        assertEquals(-1.0, swapped, EPS);
    }

    @Test
    void overallDiffIsAverageOfThreeLanes() {
        LaningResult result = new LaningResult(
                new LaneResult(3.0, List.of("Top")),
                new LaneResult(-1.0, List.of("Mid")),
                new LaneResult(1.0, List.of("Bottom"))
        );
    }

    private static Hero hero(String id, double lanePower) {
        return new Hero(
                id, id, 1,
                lanePower, 5, 5, 5, 5, 5
        );
    }
}