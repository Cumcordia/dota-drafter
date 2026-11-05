package dota.dota.analysis;

import com.example.dota.model.Hero;

public class TempoGreedBase implements ScoreComponent {
    @Override
    public double value(PhaseInput in) {
        return ScoreComponent.diffAvg(in.draft(), Hero::tempo)
                - 0.5 * ScoreComponent.diffAvg(in.draft(), Hero::greed);
    }
}