
package com.dota.model;

import java.util.List;

public record MatchDraft(
        List<Hero> radiant,
        List<Hero> dire
) {
    public MatchDraft {
        radiant = List.copyOf(radiant);
        dire = List.copyOf(dire);
    }
}
