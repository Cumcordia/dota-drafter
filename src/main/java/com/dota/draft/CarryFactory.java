package com.dota.draft;

import com.dota.model.Hero;

public class CarryFactory extends HeroFactory {

    @Override
    public Hero createHero(String name) {
        return switch (name.toLowerCase().trim()) {
            case "anti-mage" -> new Hero("anti_mage", "Anti-Mage",
                    1, 4, 2, 10, 10, 4, 5);
            case "juggernaut" -> new Hero("juggernaut", "Juggernaut",
                    1, 7, 6, 6, 8, 6, 7);
            case "phantom assassin" -> new Hero("phantom_assassin", "Phantom Assassin",
                    1, 5, 4, 9, 9, 5, 3);
            case "luna" -> new Hero("luna", "Luna",
                    1, 7, 7, 7, 8, 7, 9);
            case "faceless void" -> new Hero("faceless_void", "Faceless Void",
                    1, 5, 5, 8, 10, 9, 4);
            case "drow ranger" -> new Hero("drow_ranger", "Drow Ranger",
                    1, 7, 6, 7, 9, 6, 8);
            default -> throw new IllegalArgumentException("Unknown carry: " + name);
        };
    }
}
