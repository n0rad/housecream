package net.awired.housecream.server.it.builder.zone;

import net.awired.housecream.server.api.domain.zone.Building;

public class BuildingBuilder extends ZoneBuilder<BuildingBuilder> {

    public Building build() {
        Building building = new Building();
        build(building);
        return building;
    }

}
