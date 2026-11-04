package dota.dota.analysis;

public record PhaseResult(double diff, String label) {
    public static PhaseResult of(double diff) {
        double a = Math.abs(diff);
        String label = a < 0.5 ? "Even" : a < 1.5 ? "Slight" : "Strong";
        return new PhaseResult(diff, label);
    }
}