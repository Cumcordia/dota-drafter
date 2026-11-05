
package com.dota.laning;

import com.dota.model.Hero;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LaningAnalyzerTest {

    @Test
    void shouldCalculateLaneDifference() {
        Hero radiant = new Hero(
                "axe", "Axe", 3,
                8, 6, 4, 7, 8, 4
        );

        Hero dire = new Hero(
                "centaur", "Centaur", 3,
                6, 6, 4, 7, 8, 4
        );

        LaningAnalyzer analyzer = new LaningAnalyzer();

        double result = analyzer.calculateLanePower(
                radiant, dire
        );

        assertEquals(2.0, result);
    }


    @Test
    void shouldCalculateTopLane() {
        Hero radiantOfflane = new Hero(
                "axe", "Axe", 3,
                8, 6, 4, 7, 8, 4
        );

        Hero radiantSupport = new Hero(
                "tusk", "Tusk", 4,
                7, 8, 3, 5, 7, 3
        );

        Hero direCarry = new Hero(
                "juggernaut", "Juggernaut", 1,
                6, 6, 7, 8, 6, 7
        );

        Hero direSupport = new Hero(
                "lion", "Lion", 5,
                5, 7, 3, 6, 8, 2
        );

        LaningAnalyzer analyzer = new LaningAnalyzer();

        double result = analyzer.calculateDuoLane(
                radiantOfflane, radiantSupport,
                direCarry, direSupport
        );

        assertEquals(4.0, result);
    }


}
