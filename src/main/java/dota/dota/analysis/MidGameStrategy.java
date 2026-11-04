package dota.dota.analysis;

import dota.dota.draft.Hero;

public class MidgameStrategy implements PhaseAnalysisStrategy {
    private final ScoreComponent score;

    public MidgameStrategy() {
        ScoreComponent s = new BaseScore();
        s = new StatDiffTerm(s, Hero::tempo, 1.0);
        s = new StatDiffTerm(s, Hero::greed, -0.5);
        s = new StatDiffTerm(s, Hero::teamfight, 0.4);
        s = new StatDiffTerm(s, Hero::push, 0.2);
        s = new LaningTerm(s, 0.35);
        this.score = s;
    }

    @Override public PhaseResult analyze(PhaseInput in) { return PhaseResult.of(score.value(in)); }
}