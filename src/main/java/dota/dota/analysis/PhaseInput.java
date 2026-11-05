package dota.dota.analysis;

import dota.dota.model.MatchDraft;

public record PhaseInput(MatchDraft draft, double overallLaningDiff, double midgameDiff) {}