package dota.dota.analysis;

import dota.dota.draft.Hero;

public class LategameStrategy implements PhaseAnalysisStrategy {
    private final ScoreComponent score;

    public LategameStrategy() {
        ScoreComponent s = new BaseScore();
        s = new StatDiffTerm(s, Hero::scaling, 1.0);
        s = new StatDiffTerm(s, Hero::teamfight, 0.25);
        s = new MidgameCarryTerm(s, 0.20);
        this.score = s;
    }

    @Override public PhaseResult analyze(PhaseInput in) { return PhaseResult.of(score.value(in)); }
}