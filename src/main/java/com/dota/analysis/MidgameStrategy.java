package com.dota.analysis;

import com.dota.model.PhaseResult;

public class MidgameStrategy implements PhaseAnalysisStrategy {
    private final ScoreComponent score;

    public MidgameStrategy() {
        ScoreComponent s = new TempoGreedBase();
        s = new TeamfightModifier(s, 0.4);
        s = new PushModifier(s, 0.2);
        s = new LaningCarryoverModifier(s, 0.35);
        this.score = s;
    }

    @Override public PhaseResult analyze(PhaseInput in) { return PhaseResult.of(score.value(in)); }
}