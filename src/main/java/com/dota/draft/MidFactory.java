package com.dota.draft;

import com.dota.model.Hero;

public class MidFactory extends HeroFactory {

    @Override
    public Hero createHero(String name) {
        return switch (name.toLowerCase().trim()) {
            case "invoker" -> new Hero("invoker", "Invoker",
                    2, 6, 6, 8, 9, 9, 5);
            case "puck" -> new Hero("puck", "Puck",
                    2, 7, 9, 5, 7, 9, 4);
            case "storm spirit" -> new Hero("storm_spirit", "Storm Spirit",
                    2, 6, 7, 8, 9, 7, 3);
            case "queen of pain" -> new Hero("queen_of_pain", "Queen of Pain",
                    2, 8, 9, 5, 7, 7, 5);
            case "sniper" -> new Hero("sniper", "Sniper",
                    2, 8, 5, 8, 9, 4, 8);
            case "shadow fiend" -> new Hero("shadow_fiend", "Shadow Fiend",
                    2, 8, 8, 7, 9, 7, 9);
            default -> throw new IllegalArgumentException("Unknown mid: " + name);
        };
    }
}
