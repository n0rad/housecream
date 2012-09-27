package net.awired.housecream.server.it.builder.zone;

import net.awired.housecream.server.api.domain.zone.Area;

public class AreaBuilder extends ZoneBuilder<AreaBuilder> {

    public Area build() {
        Area area = new Area();
        build(area);
        return area;
    }

}
