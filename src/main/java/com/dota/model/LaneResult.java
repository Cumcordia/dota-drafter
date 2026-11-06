package com.dota.model;

import java.util.List;

public record LaneResult(double diff, List<String> reasons) {

    public LaneResult {
        reasons = List.copyOf(reasons);
    }
}