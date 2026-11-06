
package com.dota;

import com.dota.draft.*;

public class Main {

    public static void main(String[] args) {
        DraftBuilder builder = new DraftBuilder();

        builder.subscribe(new DraftLogger());

        builder.addHero(
                new CarryFactory().createHero("juggernaut")
        );

        builder.addHero(
                new MidFactory().createHero("puck")
        );
    }
}
