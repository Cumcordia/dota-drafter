package com.dota.draft;

import com.dota.model.Hero;

import java.util.ArrayList;
import java.util.List;

public class DraftBuilder {

    private final List<Hero> heroes = new ArrayList<>();
    private final DraftSubject subject = new DraftSubject();

    public DraftBuilder addHero(Hero hero) {
        if (hero == null) {
            throw new IllegalArgumentException("Hero cannot be null");
        }

        if (hero.id() == null || hero.id().isBlank()) {
            throw new IllegalArgumentException("Hero ID cannot be empty");
        }

        if (hero.position() < 1 || hero.position() > 5) {
            throw new IllegalArgumentException(
                    "Invalid position: " + hero.position()
            );
        }

        for (Hero selected : heroes) {
            if (selected.position() == hero.position()) {
                throw new IllegalArgumentException(
                        "Position already taken: " + hero.position()
                );
            }

            if (selected.id().equals(hero.id())) {
                throw new IllegalArgumentException(
                        "Hero already selected: " + hero.id()
                );
            }
        }

        heroes.add(hero);
        subject.notifyHeroAdded(hero);

        return this;
    }

    public List<Hero> build() {
        if (heroes.size() != 5) {
            throw new IllegalStateException(
                    "Draft must contain exactly 5 heroes"
            );
        }

        return List.copyOf(heroes);
    }

    public void subscribe(DraftObserver observer) {
        subject.subscribe(observer);
    }
    public void unsubscribe(DraftObserver observer) {
        subject.unsubscribe(observer);
    }
}