package com.dota.model;

import java.util.List;

public record AnalysisResponse(LaningResult laning, PhaseResult midgame,
                               PhaseResult lategame, double thresholdX, List<String> summary) {}