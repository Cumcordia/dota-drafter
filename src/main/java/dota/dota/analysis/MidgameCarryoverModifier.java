package dota.dota.analysis;

public class MidgameCarryoverModifier extends ScoreDecorator {
    private final double coef;
    public MidgameCarryoverModifier(ScoreComponent inner, double coef) { super(inner); this.coef = coef; }
    @Override protected double term(PhaseInput in) { return coef * in.midgameDiff(); }
}