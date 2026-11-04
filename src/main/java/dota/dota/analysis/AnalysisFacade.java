package dota.dota.analysis;

import dota.dota.draft.LaningResult;
import dota.dota.draft.MatchDraft;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class AnalysisFacade {
    public static final double THRESHOLD_X = 1.5;

    public record AnalysisResult(LaningResult laning, PhaseResult midgame,
                                 PhaseResult lategame, List<String> summary) {}

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

    public AnalysisResult analyze(MatchDraft draft) {
        LaningResult laning = laningStep.apply(draft);
        PhaseResult mid = midStrategy.analyze(new PhaseInput(draft, laning.overallDiff(), 0));
        PhaseResult late = lateStrategy.analyze(new PhaseInput(draft, laning.overallDiff(), mid.diff()));

        List<String> summary = new ArrayList<>();
        if (Math.abs(mid.diff()) >= THRESHOLD_X) {
            summary.add("Сильный перевес по темпу в мидгейме (|diff| >= " + THRESHOLD_X
                    + "): это предупреждение, а не гарантированная победа.");
        }
        return new AnalysisResult(laning, mid, late, summary);
    }
}