package com.dota.draft;

import com.dota.model.Hero;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class DraftBuilderTest {

    @Test
    void shouldBuildCompleteTeam() {
        DraftBuilder builder = completeBuilder();

        List<Hero> heroes = builder.build();

        assertEquals(5, heroes.size());
        assertEquals(5, heroes.stream()
                .map(Hero::position)
                .distinct()
                .count());
    }

    @Test
    void shouldRejectIncompleteTeam() {
        DraftBuilder builder = new DraftBuilder();

        builder.addHero(new CarryFactory().createHero("juggernaut"));

        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void shouldRejectDuplicatePosition() {
        DraftBuilder builder = new DraftBuilder();

        builder.addHero(new CarryFactory().createHero("juggernaut"));

        assertThrows(
                IllegalArgumentException.class,
                () -> builder.addHero(
                        new CarryFactory().createHero("luna")
                )
        );
    }

    @Test
    void shouldRejectDuplicateHeroId() {
        DraftBuilder builder = new DraftBuilder();

        builder.addHero(hero("same_hero", 1));

        assertThrows(
                IllegalArgumentException.class,
                () -> builder.addHero(hero("same_hero", 2))
        );
    }

    @Test
    void shouldRejectInvalidPosition() {
        for (int position : List.of(0, 6)) {
            DraftBuilder builder = new DraftBuilder();

            assertThrows(
                    IllegalArgumentException.class,
                    () -> builder.addHero(hero("test", position))
            );
        }
    }

    @Test
    void shouldRejectNullHero() {
        DraftBuilder builder = new DraftBuilder();

        assertThrows(
                IllegalArgumentException.class,
                () -> builder.addHero(null)
        );
    }

    @Test
    void shouldRejectEmptyHeroId() {
        DraftBuilder builder = new DraftBuilder();

        assertThrows(
                IllegalArgumentException.class,
                () -> builder.addHero(hero(" ", 1))
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> builder.addHero(hero(null, 1))
        );
    }

    @Test
    void shouldNotifyObserverOnlyForAcceptedHeroes() {
        DraftBuilder builder = new DraftBuilder();
        AtomicInteger notifications = new AtomicInteger();

        builder.subscribe(hero -> notifications.incrementAndGet());

        builder.addHero(new CarryFactory().createHero("juggernaut"));

        assertThrows(
                IllegalArgumentException.class,
                () -> builder.addHero(
                        new CarryFactory().createHero("luna")
                )
        );

        assertEquals(1, notifications.get());
        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void shouldReturnUnmodifiableTeam() {
        List<Hero> heroes = completeBuilder().build();

        assertThrows(
                UnsupportedOperationException.class,
                () -> heroes.remove(0)
        );
    }

    private DraftBuilder completeBuilder() {
        return new DraftBuilder()
                .addHero(new CarryFactory().createHero("juggernaut"))
                .addHero(new MidFactory().createHero("puck"))
                .addHero(new OfflaneFactory().createHero("axe"))
                .addHero(new SoftSupportFactory().createHero("tusk"))
                .addHero(new HardSupportFactory().createHero("lion"));
    }

    private Hero hero(String id, int position) {
        return new Hero(
                id, "Test", position,
                5, 5, 5, 5, 5, 5
        );
    }
}