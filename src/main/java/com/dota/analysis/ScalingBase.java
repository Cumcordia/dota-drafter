package com.dota.analysis;


import com.dota.model.Hero;

public class ScalingBase implements ScoreComponent {
    @Override
    public double value(PhaseInput in) {
        return ScoreComponent.diffAvg(in.draft(), Hero::scaling);
    }
}