package com.dota.controllers;

import com.dota.draft.CarryFactory;
import com.dota.draft.DraftBuilder;
import com.dota.draft.HardSupportFactory;
import com.dota.draft.HeroFactory;
import com.dota.draft.MidFactory;
import com.dota.draft.OfflaneFactory;
import com.dota.draft.SoftSupportFactory;
import com.dota.model.AnalyzeRequest;
import com.dota.model.Hero;
import com.dota.model.MatchDraft;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Function;

public class AnalyzeRequestMapper
        implements Function<AnalyzeRequest, MatchDraft> {

    private final List<HeroFactory> factories = List.of(
            new CarryFactory(),
            new MidFactory(),
            new OfflaneFactory(),
            new SoftSupportFactory(),
            new HardSupportFactory()
    );

    @Override
    public MatchDraft apply(AnalyzeRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        List<Hero> radiant = buildTeam(request.radiant(), true);
        List<Hero> dire = buildTeam(request.dire(), false);

        Set<String> selectedIds = new HashSet<>();

        for (Hero hero : radiant) {
            selectedIds.add(hero.id());
        }

        for (Hero hero : dire) {
            if (!selectedIds.add(hero.id())) {
                throw new IllegalArgumentException(
                        "Hero picked in both teams: " + hero.id()
                );
            }
        }

        return new MatchDraft(radiant, dire);
    }

    private List<Hero> buildTeam(
            List<AnalyzeRequest.HeroPick> picks,
            boolean radiant) {

        String team = radiant ? "Radiant" : "Dire";

        if (picks == null || picks.size() != 5) {
            throw new IllegalArgumentException(
                    team + " must contain exactly 5 heroes"
            );
        }

        DraftBuilder builder = new DraftBuilder();

        for (AnalyzeRequest.HeroPick pick : picks) {
            if (pick == null) {
                throw new IllegalArgumentException(
                        team + " contains an empty pick"
                );
            }

            if (pick.position() < 1 || pick.position() > 5) {
                throw new IllegalArgumentException(
                        "Invalid position: " + pick.position()
                );
            }

            String lane = expectedLane(pick.position(), radiant);

            if (!lane.equals(pick.lane())) {
                throw new IllegalArgumentException(
                        team + " position " + pick.position()
                                + " must be assigned to " + lane
                );
            }

            Hero hero = createHero(pick.heroId(), pick.position());
            builder.addHero(hero);
        }

        return builder.build();
    }

    private Hero createHero(String heroId, int position) {
        if (heroId == null || heroId.isBlank()) {
            throw new IllegalArgumentException("Hero ID cannot be empty");
        }

        String normalizedId = heroId.trim()
                .toLowerCase(Locale.ROOT)
                .replace('-', '_')
                .replace(' ', '_');

        String name = normalizedId.equals("anti_mage")
                ? "anti-mage"
                : normalizedId.replace('_', ' ');

        HeroFactory factory = factories.get(position - 1);

        return factory.createHero(name);
    }

    private String expectedLane(int position, boolean radiant) {
        if (position == 2) {
            return "MID";
        }

        boolean safeLaneRole = position == 1 || position == 5;

        if (radiant) {
            return safeLaneRole ? "BOTTOM" : "TOP";
        }

        return safeLaneRole ? "TOP" : "BOTTOM";
    }
}