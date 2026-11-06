package com.dota.controllers;

import com.dota.draft.*;
import com.dota.model.AnalyzeRequest;
import com.dota.model.Hero;
import com.dota.model.MatchDraft;
import java.util.*;
import java.util.function.Function;

public class AnalyzeRequestMapper implements Function<AnalyzeRequest, MatchDraft> {

    private final List<HeroFactory> factories = List.of(new CarryFactory(), new MidFactory(),
            new OfflaneFactory(), new SoftSupportFactory(), new HardSupportFactory());

    @Override
    public MatchDraft apply(AnalyzeRequest req) {
        List<Hero> radiant = buildTeam(req.radiant(), true);
        List<Hero> dire = buildTeam(req.dire(), false);

        Set<String> ids = new HashSet<>();
        radiant.forEach(h -> ids.add(h.id()));
        for (Hero h : dire) {
            if (!ids.add(h.id())) throw new IllegalArgumentException("Hero picked in both teams: " + h.id());
        }
        return new MatchDraft(radiant, dire);
    }

    private List<Hero> buildTeam(List<AnalyzeRequest.HeroPick> picks, boolean radiant) {
        if (picks == null) throw new IllegalArgumentException("Team is missing");
        DraftBuilder builder = new DraftBuilder();
        for (AnalyzeRequest.HeroPick p : picks) {
            if (p.position() < 1 || p.position() > 5) {
                throw new IllegalArgumentException("Bad position: " + p.position());
            }
            if (!expectedLane(p.position(), radiant).equals(p.lane())) {
                throw new IllegalArgumentException("Wrong lane for " + p.heroId() + ": " + p.lane());
            }
            builder.addHero(create(factories.get(p.position() - 1), p.heroId()));
        }
        try {
            return builder.build();
        } catch (IllegalStateException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    private Hero create(HeroFactory factory, String heroId) {
        String[] candidates = {
                heroId.replace('_', ' ').replace('-', ' '),
                heroId.replace('_', '-')
        };
        for (String name : candidates) {
            try {
                return factory.createHero(name);
            } catch (IllegalArgumentException ignored) {}
        }
        throw new IllegalArgumentException("Unknown hero for this position: " + heroId);
    }

    private static String expectedLane(int position, boolean radiant) {
        if (position == 2) return "MID";
        boolean duo15 = position == 1 || position == 5;
        return (duo15 == radiant) ? "BOTTOM" : "TOP";
    }
}