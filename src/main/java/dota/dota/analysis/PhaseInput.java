package dota.dota.analysis;

import com.dota.draft.MatchDraft;

public record PhaseInput(MatchDraft draft, double overallLaningDiff, double midgameDiff) {}