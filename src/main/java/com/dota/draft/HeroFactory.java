package com.dota.draft;

import com.dota.model.Hero;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

public abstract class HeroFactory {

    private static final List<Hero> HEROES = loadHeroes();

    public abstract Hero createHero(String name);

    public static List<Hero> getHeroes() {
        return HEROES;
    }

    protected Hero createFromCatalog(String name, int position) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Hero name cannot be empty");
        }

        String id = name.trim()
                .toLowerCase(Locale.ROOT)
                .replace('-', '_')
                .replace(' ', '_');

        for (Hero hero : HEROES) {
            if (hero.id().equals(id) && hero.position() == position) {
                return new Hero(
                        hero.id(),
                        hero.name(),
                        hero.position(),
                        hero.lanePower(),
                        hero.tempo(),
                        hero.greed(),
                        hero.scaling(),
                        hero.teamfight(),
                        hero.push()
                );
            }
        }

        throw new IllegalArgumentException(
                "Unknown hero for position " + position + ": " + name
        );

    }

    private static List<Hero> loadHeroes() {
        try (InputStream input = HeroFactory.class
                .getResourceAsStream("/data/heroes.json")) {

            if (input == null) {
                throw new IllegalStateException("heroes.json not found");
            }

            Hero[] heroes = new ObjectMapper()
                    .readValue(input, Hero[].class);

            return List.of(heroes);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load heroes.json", e);
        }
    }
}