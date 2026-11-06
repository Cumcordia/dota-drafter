package com.dota.model;

public record PhaseResult(double diff, String label) {
    public static PhaseResult of(double diff) {
        double a = Math.abs(diff);
        if (a < 0.5) return new PhaseResult(diff, "Even");
        String strength = a < 1.5 ? "Slight" : "Strong";
        String side = diff > 0 ? "Radiant" : "Dire";
        return new PhaseResult(diff, strength + " " + side + " Advantage");
    }
}