package dota.dota.analysis;

public class MidgameCarryTerm extends ScoreDecorator {
    private final double coef;
    public MidgameCarryTerm(ScoreComponent inner, double coef) { super(inner); this.coef = coef; }
    @Override protected double term(PhaseInput in) { return coef * in.midgameDiff(); }
}