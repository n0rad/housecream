package net.awired.housecream.server.it.api;

import net.awired.housecream.server.api.domain.zone.Land;
import net.awired.housecream.server.api.resource.ZoneResource;
import net.awired.housecream.server.api.resource.ZonesResource;
import net.awired.housecream.server.it.HcsItSession;
import net.awired.housecream.server.it.builder.zone.LandBuilder;

public class ZoneApi {

    private final HcsItSession session;

    public ZoneApi(HcsItSession session) {
        this.session = session;
    }

    public Land createLand(String name) {
        Land land = new LandBuilder().name(name).build();
        long createZone = session.getServer().getResource(ZoneResource.class, session).createZone(land);
        long zoneId = createZone;
        land.setId(zoneId);
        return land;
    }

    public void deleteAll() {
        session.getServer().getResource(ZonesResource.class, session).deleteAllZones();
    }
}
