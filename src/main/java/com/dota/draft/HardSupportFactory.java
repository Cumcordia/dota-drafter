
package com.dota.draft;

import com.dota.model.Hero;

public class HardSupportFactory extends HeroFactory {

    @Override
    public Hero createHero(String name) {
        return switch (name.toLowerCase().trim()) {
            case "crystal maiden" -> new Hero("crystal_maiden", "Crystal Maiden",
                    5, 7, 7, 2, 5, 8, 3);
            case "lion" -> new Hero("lion", "Lion",
                    5, 7, 8, 3, 6, 9, 2);
            case "dazzle" -> new Hero("dazzle", "Dazzle",
                    5, 8, 7, 3, 7, 7, 4);
            case "warlock" -> new Hero("warlock", "Warlock",
                    5, 8, 5, 4, 8, 10, 7);
            case "witch doctor" -> new Hero("witch_doctor", "Witch Doctor",
                    5, 8, 8, 3, 7, 9, 3);
            case "shadow shaman" -> new Hero("shadow_shaman", "Shadow Shaman",
                    5, 7, 9, 3, 6, 8, 10);
            default -> throw new IllegalArgumentException("Unknown hard support: " + name);
        };
    }
}
