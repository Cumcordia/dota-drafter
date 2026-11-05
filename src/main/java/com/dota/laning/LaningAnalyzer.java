package com.dota.laning;

import com.dota.model.Hero;
import com.dota.model.Hero;
import com.dota.model.MatchDraft;
import com.dota.model.LaneResult;
import com.dota.model.LaningResult;
import java.util.List;

public class LaningAnalyzer {

    public double calculateLanePower(Hero radiant, Hero dire) {
        return radiant.lanePower() - dire.lanePower();
    }


    public double calculateDuoLane(
            Hero radiant1, Hero radiant2,
            Hero dire1, Hero dire2) {

        double radiantPower =
                radiant1.lanePower() + radiant2.lanePower();

        double direPower =
                dire1.lanePower() + dire2.lanePower();

        return radiantPower - direPower;


    }

    public LaningResult analyze(MatchDraft draft) {
        List<Hero> radiant = draft.radiant();
        List<Hero> dire = draft.dire();

        double top = calculateDuoLane(
                getHero(radiant, 3),
                getHero(radiant, 4),
                getHero(dire, 1),
                getHero(dire, 5)
        );

        double mid = calculateLanePower(
                getHero(radiant, 2),
                getHero(dire, 2)
        );

        double bottom = calculateDuoLane(
                getHero(radiant, 1),
                getHero(radiant, 5),
                getHero(dire, 3),
                getHero(dire, 4)
        );

        return new LaningResult(
                new LaneResult(top, "Top lane"),
                new LaneResult(mid, "Mid lane"),
                new LaneResult(bottom, "Bottom lane")
        );
    }

    private Hero getHero(List<Hero> heroes, int position) {
        return heroes.stream()
                .filter(hero -> hero.position() == position)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Missing hero at position " + position
                        )
                );
    }


}
