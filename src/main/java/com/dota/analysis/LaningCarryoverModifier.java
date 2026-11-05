package com.dota.analysis;

public class LaningCarryoverModifier extends ScoreDecorator {
    private final double coef;
    public LaningCarryoverModifier(ScoreComponent inner, double coef) { super(inner); this.coef = coef; }
    @Override protected double term(PhaseInput in) { return coef * in.overallLaningDiff(); }
}