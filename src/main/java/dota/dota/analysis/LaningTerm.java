package dota.dota.analysis;

public class LaningTerm extends ScoreDecorator {
    private final double coef;
    public LaningTerm(ScoreComponent inner, double coef) { super(inner); this.coef = coef; }
    @Override protected double term(PhaseInput in) { return coef * in.overallLaningDiff(); }
}