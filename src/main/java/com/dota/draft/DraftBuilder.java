
package com.dota.draft;

import com.dota.model.Hero;
import java.util.ArrayList;
import java.util.List;

public class DraftBuilder {

    private final List<Hero> heroes = new ArrayList<>();

    public DraftBuilder addHero(Hero hero) {
        if (hero == null) {
            throw new IllegalArgumentException("Hero cannot be null");
        }

        for (Hero selected : heroes) {
            if (selected.position() == hero.position()) {
                throw new IllegalArgumentException("Position already taken");
            }

            if (selected.id().equals(hero.id())) {
                throw new IllegalArgumentException("Hero already selected");
            }
        }

        heroes.add(hero);
        subject.notifyHeroAdded(hero);
        return this;
    }

    public List<Hero> build() {
        if (heroes.size() != 5) {
            throw new IllegalStateException("Draft must contain 5 heroes");
        }

        return List.copyOf(heroes);
    }

    private final DraftSubject subject = new DraftSubject();

    public void subscribe(DraftObserver observer) {
        subject.subscribe(observer);
    }


}
