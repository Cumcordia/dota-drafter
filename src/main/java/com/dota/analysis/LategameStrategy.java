package com.dota.analysis;

import com.dota.model.PhaseResult;

public class LategameStrategy implements PhaseAnalysisStrategy {
    private final ScoreComponent score;

    public LategameStrategy() {
        ScoreComponent s = new ScalingBase();
        s = new TeamfightModifier(s, 0.25);
        s = new MidgameCarryoverModifier(s, 0.20);
        this.score = s;
    }

    @Override public PhaseResult analyze(PhaseInput in) { return PhaseResult.of(score.value(in)); }
}