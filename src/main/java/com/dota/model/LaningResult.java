package com.dota.model;

public record LaningResult(
        LaneResult top,
        LaneResult mid,
        LaneResult bottom
) {
    public double overallDiff() {
        return (top.diff() + mid.diff() + bottom.diff()) / 3.0;
    }
}