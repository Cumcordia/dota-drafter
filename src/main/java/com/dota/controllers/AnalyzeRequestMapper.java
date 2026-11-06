package com.dota.controllers;

import com.dota.model.AnalyzeRequest;
import com.dota.model.Hero;
import com.dota.model.MatchDraft;
import java.util.*;
import java.util.function.Function;

public class AnalyzeRequestMapper implements Function<AnalyzeRequest, MatchDraft> {
    private final Function<String, Hero> heroById;

    public AnalyzeRequestMapper(Function<String, Hero> heroById) { this.heroById = heroById; }

    @Override
    public MatchDraft apply(AnalyzeRequest req) {
        List<Hero> radiant = toHeroes(req.radiant(), true);
        List<Hero> dire = toHeroes(req.dire(), false);
        Set<String> ids = new HashSet<>();
        for (Hero h : radiant) ids.add(h.id());
        for (Hero h : dire) {
            if (!ids.add(h.id())) throw new IllegalArgumentException("Duplicate hero: " + h.id());
        }
        return new MatchDraft(radiant, dire);
    }

    private List<Hero> toHeroes(List<AnalyzeRequest.HeroPick> picks, boolean radiant) {
        if (picks == null || picks.size() != 5) {
            throw new IllegalArgumentException("Each team needs exactly 5 heroes");
        }
        List<Hero> result = new ArrayList<>();
        Set<Integer> positions = new HashSet<>();
        for (AnalyzeRequest.HeroPick p : picks) {
            Hero hero = heroById.apply(p.heroId());
            if (hero == null) throw new IllegalArgumentException("Unknown hero: " + p.heroId());
            if (hero.position() != p.position()) {
                throw new IllegalArgumentException(p.heroId() + " is pos " + hero.position()
                        + ", not " + p.position());
            }
            if (!positions.add(p.position())) {
                throw new IllegalArgumentException("Duplicate position " + p.position());
            }
            if (!expectedLane(p.position(), radiant).equals(p.lane())) {
                throw new IllegalArgumentException("Wrong lane for " + p.heroId() + ": " + p.lane());
            }
            result.add(hero);
        }
        return result;
    }

    private static String expectedLane(int position, boolean radiant) {
        if (position == 2) return "MID";
        boolean duo15 = position == 1 || position == 5;
        return (duo15 == radiant) ? "BOTTOM" : "TOP";
    }
}