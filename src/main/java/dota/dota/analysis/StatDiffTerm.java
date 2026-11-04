package dota.dota.analysis;

import dota.dota.draft.Hero;
import java.util.List;
import java.util.function.ToDoubleFunction;

public class StatDiffTerm extends ScoreDecorator {
    private final ToDoubleFunction<Hero> stat;
    private final double coef;

    public StatDiffTerm(ScoreComponent inner, ToDoubleFunction<Hero> stat, double coef) {
        super(inner);
        this.stat = stat;
        this.coef = coef;
    }

    @Override
    protected double term(PhaseInput in) {
        return coef * (avg(in.draft().radiant()) - avg(in.draft().dire()));
    }

    private double avg(List<Hero> heroes) {
        return heroes.stream().mapToDouble(stat).average().orElse(0.0);
    }
}