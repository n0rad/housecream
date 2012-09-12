package net.awired.housecream.server.it.builder.zone;

import net.awired.housecream.server.api.domain.zone.Land;

public class LandBuilder extends ZoneBuilder<LandBuilder> {

    public Land build() {
        Land land = new Land();
        build(land);
        return land;
    }

}
