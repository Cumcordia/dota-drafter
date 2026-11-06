package com.dota.laning;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class LaneRuleCatalog {

    private static final List<Rule> SYNERGIES =
            loadRules("/data/synergy.json");

    private static final List<Rule> MATCHUPS =
            loadRules("/data/matchups.json");

    public static double synergy(String first, String second) {
        for (Rule rule : SYNERGIES) {
            boolean direct = rule.first().equals(first)
                    && rule.second().equals(second);

            boolean reverse = rule.first().equals(second)
                    && rule.second().equals(first);

            if (direct || reverse) {
                return rule.bonus();
            }
        }

        return 0.0;
    }

    public static double matchup(String first, String second) {
        for (Rule rule : MATCHUPS) {
            if (rule.first().equals(first)
                    && rule.second().equals(second)) {
                return rule.bonus();
            }

            if (rule.first().equals(second)
                    && rule.second().equals(first)) {
                return -rule.bonus();
            }
        }

        return 0.0;
    }

    private static List<Rule> loadRules(String path) {
        try (InputStream input = LaneRuleCatalog.class
                .getResourceAsStream(path)) {

            if (input == null) {
                throw new IllegalStateException(
                        "Rule file not found: " + path
                );
            }

            Rule[] rules = new ObjectMapper()
                    .readValue(input, Rule[].class);

            return List.of(rules);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Cannot load rules: " + path,
                    e
            );
        }
    }

    public record Rule(
            String first,
            String second,
            double bonus
    ) {
    }
}