package com.dota.laning;

import com.dota.model.Hero;

public class LaningAnalyzer {

    public double calculateLanePower(Hero radiant, Hero dire) {
        return radiant.lanePower() - dire.lanePower();
    }
}
