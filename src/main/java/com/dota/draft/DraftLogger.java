
package com.dota.draft;

import com.dota.model.Hero;

public class DraftLogger implements DraftObserver {

    @Override
    public void onHeroAdded(Hero hero) {
        System.out.println("Hero added: " + hero.name());
    }
}
