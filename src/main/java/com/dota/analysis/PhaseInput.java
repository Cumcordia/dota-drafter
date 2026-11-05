package com.dota.analysis;

import com.dota.model.MatchDraft;

public record PhaseInput(MatchDraft draft, double overallLaningDiff, double midgameDiff) {}