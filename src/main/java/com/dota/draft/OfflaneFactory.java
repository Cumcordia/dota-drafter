
package com.dota.draft;

import com.dota.model.Hero;

public class OfflaneFactory extends HeroFactory {

    @Override
    public Hero createHero(String name) {
        return switch (name.toLowerCase().trim()) {
            case "axe" -> new Hero("axe", "Axe",
                    3, 7, 8, 4, 7, 9, 3);
            case "mars" -> new Hero("mars", "Mars",
                    3, 8, 8, 5, 7, 9, 5);
            case "tidehunter" -> new Hero("tidehunter", "Tidehunter",
                    3, 7, 6, 5, 8, 10, 4);
            case "centaur warrunner" -> new Hero("centaur_warrunner", "Centaur Warrunner",
                    3, 8, 8, 4, 7, 8, 6);
            case "underlord" -> new Hero("underlord", "Underlord",
                    3, 8, 6, 5, 8, 8, 9);
            case "bristleback" -> new Hero("bristleback", "Bristleback",
                    3, 9, 8, 6, 8, 6, 7);
            default -> throw new IllegalArgumentException("Unknown offlane: " + name);
        };
    }
}
