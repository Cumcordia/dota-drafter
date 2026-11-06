package com.dota.laning;

import com.dota.model.Hero;
import com.dota.model.LaneResult;
import com.dota.model.LaningResult;
import com.dota.model.MatchDraft;

import java.util.ArrayList;
import java.util.List;

public class LaningAnalyzer {

    public double calculateLanePower(Hero radiant, Hero dire) {
        return radiant.lanePower() - dire.lanePower();
    }

    public double calculateDuoLane(
            Hero radiant1, Hero radiant2,
            Hero dire1, Hero dire2) {

        return analyzeDuoLane(
                radiant1, radiant2, dire1, dire2
        ).diff();
    }

    public LaningResult analyze(MatchDraft draft) {
        List<Hero> radiant = draft.radiant();
        List<Hero> dire = draft.dire();

        LaneResult top = analyzeDuoLane(
                getHero(radiant, 3),
                getHero(radiant, 4),
                getHero(dire, 1),
                getHero(dire, 5)
        );

        LaneResult mid = analyzeMidLane(
                getHero(radiant, 2),
                getHero(dire, 2)
        );

        LaneResult bottom = analyzeDuoLane(
                getHero(radiant, 1),
                getHero(radiant, 5),
                getHero(dire, 3),
                getHero(dire, 4)
        );

        return new LaningResult(top, mid, bottom);
    }

    private LaneResult analyzeDuoLane(
            Hero radiant1, Hero radiant2,
            Hero dire1, Hero dire2) {

        List<String> reasons = new ArrayList<>();

        double radiantBase =
                (radiant1.lanePower() + radiant2.lanePower()) / 2.0;

        double direBase =
                (dire1.lanePower() + dire2.lanePower()) / 2.0;

        double baseDiff = radiantBase - direBase;

        reasons.add(
                "Average lane power: Radiant " + radiantBase
                        + ", Dire " + direBase
                        + "; difference: " + baseDiff
        );

        double radiantSynergy = calculateSynergy(radiant1, radiant2);
        double direSynergy = calculateSynergy(dire1, dire2);

        if (radiantSynergy != 0.0) {
            reasons.add(
                    "Radiant synergy: " + radiant1.name()
                            + " + " + radiant2.name()
                            + "; contribution: " + radiantSynergy
            );
        }

        if (direSynergy != 0.0) {
            reasons.add(
                    "Dire synergy: " + dire1.name()
                            + " + " + dire2.name()
                            + "; contribution: " + (-direSynergy)
            );
        }

        double rawCounter = 0.0;

        rawCounter += addMatchupReason(radiant1, dire1, reasons);
        rawCounter += addMatchupReason(radiant1, dire2, reasons);
        rawCounter += addMatchupReason(radiant2, dire1, reasons);
        rawCounter += addMatchupReason(radiant2, dire2, reasons);

        double counter = Math.max(-1.0, Math.min(1.0, rawCounter));

        if (counter != rawCounter) {
            reasons.add(
                    "Counter adjustment limited from "
                            + rawCounter + " to " + counter
            );
        }

        double diff =
                baseDiff + radiantSynergy - direSynergy + counter;

        return new LaneResult(diff, reasons);
    }

    private LaneResult analyzeMidLane(Hero radiant, Hero dire) {
        List<String> reasons = new ArrayList<>();

        double baseDiff = calculateLanePower(radiant, dire);

        reasons.add(
                "Lane power: " + radiant.name()
                        + " " + radiant.lanePower()
                        + " vs " + dire.name()
                        + " " + dire.lanePower()
                        + "; difference: " + baseDiff
        );

        double counter = addMatchupReason(radiant, dire, reasons);

        return new LaneResult(baseDiff + counter, reasons);
    }

    private double addMatchupReason(
            Hero radiant,
            Hero dire,
            List<String> reasons) {

        double bonus = calculateMatchupBonus(radiant, dire);

        if (bonus != 0.0) {
            reasons.add(
                    "Matchup: Radiant " + radiant.name()
                            + " vs Dire " + dire.name()
                            + "; contribution before lane limit: " + bonus
            );
        }

        return bonus;
    }

    private Hero getHero(List<Hero> heroes, int position) {
        return heroes.stream()
                .filter(hero -> hero.position() == position)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Missing hero at position " + position
                ));
    }

    public double calculateSynergy(Hero first, Hero second) {
        return LaneRuleCatalog.synergy(first.id(), second.id());
    }

    public double calculateMatchupBonus(Hero radiant, Hero dire) {
        return LaneRuleCatalog.matchup(radiant.id(), dire.id());
    }
}