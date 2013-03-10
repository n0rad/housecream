package net.awired.housecream.server.it.api;

import net.awired.housecream.server.api.domain.zone.Land;
import net.awired.housecream.server.api.resource.ZonesResource;
import net.awired.housecream.server.it.HcWsItSession;
import net.awired.housecream.server.it.builder.zone.LandBuilder;

public class ZoneApi {

    private final HcWsItSession session;

    public ZoneApi(HcWsItSession session) {
        this.session = session;
    }

    public Land createLand(String name) {
        Land land = (Land) session.getServer().getResource(ZonesResource.class, session)
                .createZone(new LandBuilder().name(name).build());
        return land;
    }

    public void deleteAll() {
        session.getServer().getResource(ZonesResource.class, session).deleteAllZones();
    }
}
