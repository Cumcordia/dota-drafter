package com.dota.draft;

import com.dota.model.Hero;

public class CarryFactory extends HeroFactory {

    @Override
    public Hero createHero(String name) {
        return createFromCatalog(name, 1);
    }
}