package com.dota.model;


import java.util.List;

public record Draft(
        List<Hero> radiant,
        List<Hero> dire
) {
    public Draft {
        radiant = List.copyOf(radiant);
        dire = List.copyOf(dire);
    }
}