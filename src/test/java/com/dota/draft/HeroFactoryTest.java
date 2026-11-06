package com.dota.draft;

import com.dota.model.Hero;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HeroFactoryTest {

    @Test
    void shouldLoadThirtyUniqueHeroes() {
        List<Hero> heroes = HeroFactory.getHeroes();

        assertEquals(30, heroes.size());

        assertEquals(
                30L,
                heroes.stream().map(Hero::id).distinct().count()
        );
    }

    @Test
    void shouldHaveSixHeroesForEachPosition() {
        List<Hero> heroes = HeroFactory.getHeroes();

        for (int position = 1; position <= 5; position++) {
            int currentPosition = position;

            long count = heroes.stream()
                    .filter(hero -> hero.position() == currentPosition)
                    .count();

            assertEquals(6L, count);
        }
    }

    @Test
    void shouldHaveValidHeroData() {
        for (Hero hero : HeroFactory.getHeroes()) {
            assertNotNull(hero.id());
            assertFalse(hero.id().isBlank());

            assertNotNull(hero.name());
            assertFalse(hero.name().isBlank());

            assertTrue(hero.position() >= 1 && hero.position() <= 5);

            double[] stats = {
                    hero.lanePower(),
                    hero.tempo(),
                    hero.greed(),
                    hero.scaling(),
                    hero.teamfight(),
                    hero.push()
            };

            for (double stat : stats) {
                assertTrue(Double.isFinite(stat));
                assertTrue(stat >= 0 && stat <= 10);
            }
        }
    }

    @Test
    void factoriesShouldCreateHeroesFromCatalog() {
        List<HeroFactory> factories = List.of(
                new CarryFactory(),
                new MidFactory(),
                new OfflaneFactory(),
                new SoftSupportFactory(),
                new HardSupportFactory()
        );

        for (Hero expected : HeroFactory.getHeroes()) {
            HeroFactory factory = factories.get(expected.position() - 1);

            Hero actual = factory.createHero(expected.id());

            assertEquals(expected, actual);
        }
    }

    @Test
    void shouldRejectHeroFromWrongPosition() {
        CarryFactory factory = new CarryFactory();

        assertThrows(
                IllegalArgumentException.class,
                () -> factory.createHero("axe")
        );
    }

    @Test
    void shouldRejectUnknownHero() {
        CarryFactory factory = new CarryFactory();

        assertThrows(
                IllegalArgumentException.class,
                () -> factory.createHero("unknown_hero")
        );
    }
}