package com.dota.analysis;

import com.dota.model.Hero;

public class TeamfightModifier extends ScoreDecorator {
    private final double coef;
    public TeamfightModifier(ScoreComponent inner, double coef) { super(inner); this.coef = coef; }
    @Override protected double term(PhaseInput in) {
        return coef * ScoreComponent.diffAvg(in.draft(), Hero::teamfight);
    }
}