
package com.dota.draft;

import com.dota.model.Hero;

public class SoftSupportFactory extends HeroFactory {

    @Override
    public Hero createHero(String name) {
        return switch (name.toLowerCase().trim()) {
            case "earthshaker" -> new Hero("earthshaker", "Earthshaker",
                    4, 5, 7, 5, 8, 10, 3);
            case "tusk" -> new Hero("tusk", "Tusk",
                    4, 8, 9, 3, 5, 8, 3);
            case "rubick" -> new Hero("rubick", "Rubick",
                    4, 6, 7, 5, 8, 9, 3);
            case "nyx assassin" -> new Hero("nyx_assassin", "Nyx Assassin",
                    4, 6, 8, 3, 6, 7, 2);
            case "hoodwink" -> new Hero("hoodwink", "Hoodwink",
                    4, 8, 8, 5, 7, 7, 5);
            case "spirit breaker" -> new Hero("spirit_breaker", "Spirit Breaker",
                    4, 7, 9, 3, 7, 8, 3);
            default -> throw new IllegalArgumentException("Unknown soft support: " + name);
        };
    }
}
