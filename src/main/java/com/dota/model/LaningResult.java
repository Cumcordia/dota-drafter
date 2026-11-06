package com.dota.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LaningResult(
        LaneResult top,
        LaneResult mid,
        LaneResult bottom
) {

    @JsonProperty("overallDiff")
    public double overallDiff() {
        return (top.diff() + mid.diff() + bottom.diff()) / 3.0;
    }
}