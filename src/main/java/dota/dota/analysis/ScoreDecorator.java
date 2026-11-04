package dota.dota.analysis;

public abstract class ScoreDecorator implements ScoreComponent {
    private final ScoreComponent inner;

    protected ScoreDecorator(ScoreComponent inner) { this.inner = inner; }

    @Override
    public final double value(PhaseInput in) { return inner.value(in) + term(in); }

    /** Одно отдельное слагаемое формулы. */
    protected abstract double term(PhaseInput in);
}