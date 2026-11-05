package com.dota.draft;

import com.dota.model.Hero;

public interface DraftObserver {
    void onHeroAdded(Hero hero);
}
