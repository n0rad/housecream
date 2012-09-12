package net.awired.housecream.server.it.builder.zone;

import net.awired.housecream.server.api.domain.zone.Floor;

public class FloorBuilder extends ZoneBuilder<FloorBuilder> {

    public Floor build() {
        Floor floor = new Floor();
        build(floor);
        return floor;
    }

}
