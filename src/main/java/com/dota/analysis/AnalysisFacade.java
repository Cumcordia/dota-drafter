package com.dota.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.dota.model.*;

public class AnalysisFacade {
    public static final double THRESHOLD_X = 1.5;

    private final Function<MatchDraft, LaningResult> laningStep; // модуль A
    private final PhaseAnalysisStrategy midStrategy;
    private final PhaseAnalysisStrategy lateStrategy;

    public AnalysisFacade(Function<MatchDraft, LaningResult> laningStep,
                          PhaseAnalysisStrategy midStrategy,
                          PhaseAnalysisStrategy lateStrategy) {
        this.laningStep = laningStep;
        this.midStrategy = midStrategy;
        this.lateStrategy = lateStrategy;
    }

    /** Драфт уже провалидирован A при сборке MatchDraft. */
    public AnalysisResponse analyze(MatchDraft draft) {
        LaningResult laning = laningStep.apply(draft);                       // 1) laning (один раз)
        PhaseResult mid = midStrategy.analyze(
                new PhaseInput(draft, laning.overallDiff(), 0));             // 2) mid <- laning
        PhaseResult late = lateStrategy.analyze(
                new PhaseInput(draft, laning.overallDiff(), mid.diff()));    // 3) late <- mid
        return new AnalysisResponse(laning, mid, late, THRESHOLD_X, summary(draft, mid));
    }

    private List<String> summary(MatchDraft d, PhaseResult mid) {
        List<String> s = new ArrayList<>();
        addSide(s, ScoreComponent.diffAvg(d, Hero::tempo), "better tempo");
        addSide(s, ScoreComponent.diffAvg(d, Hero::scaling), "better scaling");
        if (Math.abs(mid.diff()) >= THRESHOLD_X) {
            String side = mid.diff() > 0 ? "Radiant" : "Dire";
            s.add("Сильный темповый перевес у " + side
                    + "; сопернику будет сложнее реализовать лейт");
        }
        return s;
    }

    private static void addSide(List<String> s, double diff, String what) {
        if (Math.abs(diff) >= 0.5) s.add((diff > 0 ? "Radiant" : "Dire") + " has " + what);
    }
}