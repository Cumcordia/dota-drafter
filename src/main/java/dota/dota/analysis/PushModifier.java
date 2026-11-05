package dota.dota.analysis;

import dota.dota.model.Hero;

public class PushModifier extends ScoreDecorator {
    private final double coef;
    public PushModifier(ScoreComponent inner, double coef) { super(inner); this.coef = coef; }
    @Override protected double term(PhaseInput in) {
        return coef * ScoreComponent.diffAvg(in.draft(), Hero::push);
    }
}