package dota.dota.analysis;

import dota.dota.model.Hero;
import dota.dota.model.MatchDraft;
import java.util.List;
import java.util.function.ToDoubleFunction;

public interface ScoreComponent {
    double value(PhaseInput in);

    static double diffAvg(MatchDraft d, ToDoubleFunction<Hero> stat) {
        return avg(d.radiant(), stat) - avg(d.dire(), stat);
    }

    private static double avg(List<Hero> heroes, ToDoubleFunction<Hero> stat) {
        return heroes.stream().mapToDouble(stat).average().orElse(0.0);
    }
}