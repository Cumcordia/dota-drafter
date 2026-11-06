package com.dota.draft;

import com.dota.model.Hero;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DraftObserverTest {

    @Test
    void shouldNotifyAllSubscribedObservers() {
        DraftBuilder builder = new DraftBuilder();

        List<Hero> firstListener = new ArrayList<>();
        List<Hero> secondListener = new ArrayList<>();

        builder.subscribe(firstListener::add);
        builder.subscribe(secondListener::add);

        Hero juggernaut = new CarryFactory().createHero("juggernaut");
        Hero puck = new MidFactory().createHero("puck");

        builder.addHero(juggernaut);
        builder.addHero(puck);

        List<Hero> expected = List.of(juggernaut, puck);

        assertEquals(expected, firstListener);
        assertEquals(expected, secondListener);
    }

    @Test
    void shouldStopNotifyingUnsubscribedObserver() {
        DraftBuilder builder = new DraftBuilder();

        List<Hero> received = new ArrayList<>();
        DraftObserver observer = received::add;

        builder.subscribe(observer);

        Hero juggernaut = new CarryFactory().createHero("juggernaut");
        builder.addHero(juggernaut);

        builder.unsubscribe(observer);

        Hero puck = new MidFactory().createHero("puck");
        builder.addHero(puck);

        assertEquals(List.of(juggernaut), received);
    }
}