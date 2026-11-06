package com.dota.analysis;

import com.dota.model.PhaseResult;

public interface PhaseAnalysisStrategy {
    PhaseResult analyze(PhaseInput in);
}