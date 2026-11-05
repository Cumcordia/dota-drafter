package dota.dota.analysis;

import dota.dota.model.PhaseResult;

public interface PhaseAnalysisStrategy {
    PhaseResult analyze(PhaseInput in);
}