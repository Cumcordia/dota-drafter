package com.dota.laning;

import com.dota.model.Hero;

public class LaningAnalyzer {

    public double calculateLanePower(Hero radiant, Hero dire) {
        return radiant.lanePower() - dire.lanePower();
    }


    public double calculateDuoLane(
            Hero radiant1, Hero radiant2,
            Hero dire1, Hero dire2) {

        double radiantPower =
                radiant1.lanePower() + radiant2.lanePower();

        double direPower =
                dire1.lanePower() + dire2.lanePower();

        return radiantPower - direPower;
    }

}
