
package com.dota.draft;

import com.dota.model.Hero;
import java.util.ArrayList;
import java.util.List;

public class DraftSubject {

    private final List<DraftObserver> observers = new ArrayList<>();

    public void subscribe(DraftObserver observer) {
        observers.add(observer);
    }

    public void unsubscribe(DraftObserver observer) {
        observers.remove(observer);
    }

    public void notifyHeroAdded(Hero hero) {
        for (DraftObserver observer : List.copyOf(observers)) {
            observer.onHeroAdded(hero);
        }
    }
}
